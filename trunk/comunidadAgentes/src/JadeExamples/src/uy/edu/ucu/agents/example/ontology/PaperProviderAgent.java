package JadeExamples.src.uy.edu.ucu.agents.example.ontology;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public final class PaperProviderAgent extends Agent {
	
	private final Paper[] papers;

	public PaperProviderAgent(Paper[] papers) {
		this.papers = papers;
	}
	
	@Override
	protected void setup() {
		super.setup();
		
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(PaperOntology.getInstance());
		
		addBehaviour(new PublishPaper (papers));
		
		System.out.println (getName() + ": Iniciado");
	}

	/**
	 * A <code>Behaviour</code> that waits for a finder to query for a paper 
	 * and returns the paper if it matches the title of any of the provided papers.
	 * @author saguiar
	 *
	 */
	private static final class PublishPaper extends Behaviour {

		private final Paper[] papers;				
		
		public PublishPaper(Paper[] papers) {
			assert papers != null && papers.length > 0: "Paper array must not be null or empty";			
			
			this.papers = papers;			
		}

		@Override
		public void action() {			
			
			MessageTemplate template = 
				MessageTemplate.and (			
						MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF),
						MessageTemplate.and(
								MessageTemplate.MatchOntology(PaperOntology.getInstance().getName()),
								MessageTemplate.MatchLanguage(new SLCodec().getName())));
			
			System.out.println (myAgent.getName() + ": Esperando pedido.");
			
			ACLMessage message = myAgent.blockingReceive(template);
			assert message != null : "Response must not be null after blocking receive";
			
			System.out.println (myAgent.getName() + ": Procesando pedido.");
			
			ContentElement ce = null;
			try {
				ce = myAgent.getContentManager().extractContent(message);			
			} catch (CodecException e1) {
				e1.printStackTrace();
			} catch (OntologyException e1) {
				e1.printStackTrace();
			}
			
			if (ce == null
				|| !Provides.class.isInstance(ce)) {
				System.out.println (myAgent.getName() + ": No comprend� el mensaje.");
				sendNotUnderstood(message);
				return;
			}						
				
			Provides query = (Provides) ce;
			assert query.getPaper() != null : "Violated ontology constraint";
			
			Paper paper = findPaper (query.getPaper());
			if (paper != null) {
				System.out.println (myAgent.getName() + ": " +
						"Informando que proveo el paper '" + 
						paper.getTitle() + "'");
				sendInformProvides (message, paper);					
			} else {
				System.out.println (myAgent.getName() + ": No proveo el paper.");				
			}			
		}

		@Override
		public boolean done() {
			return false;
		}
		
		private boolean isSamePaper (Paper paper1, Paper paper2) {
			assert paper1 != null && paper2 != null;
			
			return (paper1.getTitle() != null && paper1.getTitle().equals(paper2.getTitle()));
		}
		
		private Paper findPaper (Paper requestedPaper) {
			assert requestedPaper != null;
			
			for (Paper paper: papers) {
				if (isSamePaper (requestedPaper, paper)) { 
					return paper;						
				}
			}
			return null;
		}
		
		private void sendInformProvides (ACLMessage message, Paper paper) {
			assert message != null && paper != null;
			
			ACLMessage reply = message.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			
			Provides provides = new Provides(myAgent.getAID(), paper);
			try {				
				myAgent.getContentManager().fillContent(reply, provides);
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			myAgent.send(reply);
		}	
		
		private void sendNotUnderstood (ACLMessage message) {
			assert message != null;
			
			ACLMessage reply = message.createReply();
			reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			myAgent.send(reply);
		}
	}
	
	
}
