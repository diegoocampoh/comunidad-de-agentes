package JadeExamples.src.uy.edu.ucu.agents.example.protocol;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

public final class PaperFinderAgent extends Agent {		
		
	private final Paper paper;
	
	public PaperFinderAgent(Paper paper) {
		this.paper = paper;
	}
	
	@Override
	protected void setup() {
		super.setup();											
		
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(PaperOntology.getInstance());
		
		addBehaviour(new FindPaper(this, paper));
		
		System.out.println (getName() + ": Iniciado");
	}
	
	/**
	 * Implementa el rol iniciador del protocolo FIPA Query para preguntar si 
	 * otro agente tiene un paper dado.
	 * @author saguiar
	 *
	 */
	private static final class FindPaper extends SimpleAchieveREInitiator {

		private final Paper paper;
		
		FindPaper (Agent finder, Paper paper) {
			super(finder, null);
			this.paper = paper;						
		}

		@Override
		protected void handleAgree(ACLMessage msg) {
			System.out.println (myAgent.getName() + ": El provedor acept� mi pregunta.");			
		}
		
		@Override
		protected void handleFailure(ACLMessage msg) {
			System.out.println (myAgent.getName() + ": El provedor no logro informarme si tienen o no el paper.");
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			ContentElement ce = null;
			try {
				ce = myAgent.getContentManager().extractContent(inform);
				if (Provides.class.isInstance(ce)) {
					System.out.println (myAgent.getName() + ": Encontr� el paper!");		
				} else if (Not.class.isInstance(ce)) {
					System.out.println (myAgent.getName() + ": No encontr� el paper!");
				} else {
					System.out.println("Predicado desconocido");
				}
			} catch (CodecException e1) {
				e1.printStackTrace();
			} catch (OntologyException e1) {
				e1.printStackTrace();
			}			
		}

		@Override
		protected void handleNotUnderstood(ACLMessage msg) {
			System.out.println (myAgent.getName() + ": El provedor no entendi� mi mensaje");
		}

		@Override
		protected void handleRefuse(ACLMessage msg) {
			System.out.println (myAgent.getName() + ": El provedor se neg� a responderme: " + msg.getContent());
		}

		@Override
		protected ACLMessage prepareRequest(ACLMessage msg) {
			
			AID provider = new AID("PaperProvider", false);
			
			ACLMessage query = new ACLMessage(ACLMessage.QUERY_IF);
			
			query.addReceiver(provider);
			query.setOntology(PaperOntology.getInstance().getName());
			query.setLanguage(new SLCodec().getName());
			
			Provides provides = new Provides(provider, paper);
			
			try {
				myAgent.getContentManager().fillContent(query, provides);
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}						
			
			System.out.println(myAgent.getName() + ": Buscando '" + provides.getPaper().getTitle() + "'.");
			
			return query;
		}
	}
	
}
