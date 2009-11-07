package JadeExamples.src.uy.edu.ucu.agents.example.mobility;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public final class PaperProviderAgent extends Agent {		
	
	private final Codec codec = new SLCodec();
	
	private final Ontology ontology = PaperOntology.getInstance();

	private final Paper[] papers;
	
	public PaperProviderAgent(Paper[] papers) {
		this.papers = papers;
	}

	@Override
	protected void setup() {
		super.setup();
		
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(PaperOntology.getInstance());
		
		addBehaviour(new PublishPaper (this, papers, codec, ontology));
		
		System.out.println (getName() + ": Iniciado");
	}
	
	/**
	 * Implementa el rol de quien responde del protocolo FIPA Query para preguntar si 
	 * otro agente tiene un paper dado.
	 * @author saguiar
	 *
	 */
	private static final class PublishPaper extends SimpleAchieveREResponder {
	
		private final Paper[] papers;							
		
		public PublishPaper(Agent agent, Paper[] papers, Codec codec, Ontology ontology) {
			super (agent, MessageTemplate.and (			
						MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF),
						MessageTemplate.and(
								MessageTemplate.MatchOntology(ontology.getName()),
								MessageTemplate.MatchLanguage(codec.getName()))));						
			this.papers = papers;			
		}

		private boolean isSamePaper (Paper paper1, Paper paper2) {
			assert paper1 != null && paper2 != null;
			
			return (paper1.getTitle() != null && paper1.getTitle().equals(paper2.getTitle()));
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
			Provides query = getProvides (request);					
			assert query.getPaper() != null : "Violated ontology constraint";
			
			if (!query.getProvider().equals(myAgent.getAID())) {
				throw new RefuseException("Not for me");
			} else {
				ACLMessage reply = request.createReply();
				reply.setPerformative(ACLMessage.AGREE);
				return reply;
			}			
		}
		
		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
			Provides query;
			try {
				query = getProvides (request);
			} catch (NotUnderstoodException e1) {
				throw new FailureException("Previously understood message was not understood");
			}
						
			try {
				ACLMessage reply = request.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				
				if (providesPaper (query.getProvider(), query.getPaper())) {										
					System.out.println(myAgent.getName() + ": Proveo el paper '" + query.getPaper().getTitle() + "'.");
					myAgent.getContentManager().fillContent(reply, query);											
				} else {
					System.out.println(myAgent.getName() + ": No proveo el paper '" + query.getPaper().getTitle() + "'.");
					myAgent.getContentManager().fillContent(reply, new Not (query));				
				}
				
				return reply;
				
			} catch (CodecException e) {
				throw new FailureException (e.getMessage());
			} catch (OntologyException e) {
				throw new FailureException (e.getMessage());
			}						
		}
		
		private Provides getProvides (ACLMessage request) throws NotUnderstoodException {
			ContentElement ce = null;
			try {
				ce = myAgent.getContentManager().extractContent(request);			
			} catch (CodecException e1) {
				throw new NotUnderstoodException (e1.getMessage());
			} catch (OntologyException e1) {
				throw new NotUnderstoodException (e1.getMessage());
			}
			
			if (!Provides.class.isInstance(ce)) {
				System.out.println (myAgent.getName() + ": No comprend� el mensaje.");
				throw new NotUnderstoodException("Invalid predicate instance");				
			}						
				
			Provides query = (Provides) ce;			
			return query;
		}
		
		private boolean providesPaper (AID provider, Paper requestedPaper) {
			assert requestedPaper != null;
			
			if (!provider.equals(myAgent.getAID())) {
				return false;				
			}
			
			for (Paper paper: papers) {
				if (isSamePaper (requestedPaper, paper)) { 
					return true;						
				}
			}
			return false;
		}
	}
	
}
