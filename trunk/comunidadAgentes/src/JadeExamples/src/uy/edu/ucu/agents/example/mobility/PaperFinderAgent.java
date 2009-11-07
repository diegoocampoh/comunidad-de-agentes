package JadeExamples.src.uy.edu.ucu.agents.example.mobility;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.WhereIsAgentAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import jade.wrapper.ControllerException;

/**
 * Agente que pregunta a proveedor de papers si tiene un paper dado, y en 
 * caso afirmativo se mueve a donde se encuentra el proveedor de papers.
 * @author saguiar
 *
 */
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
		getContentManager().registerOntology(MobilityOntology.getInstance());
						
		addBehaviour(new FindPaper(this, paper));
								
		System.out.println (getName() + ": Iniciado");
	}
	
	/**
	 * Implementa el rol iniciador del protocolo FIPA Query para preguntar al
	 * AMS (Agent Management System) la localizaci�n de un agente identificado
	 * por su <code>AID</code> y, si se encuentr�, mover el agente a esa
	 * localizaci�n. 
	 * 
	 * @author saguiar
	 *
	 */
	private static final class MoveToAgent extends SimpleAchieveREInitiator {

		private final AID destination;
		
		MoveToAgent(Agent agent, AID destination) {
			super (agent, null);
			this.destination = destination;
		}
		
		@Override
		protected ACLMessage prepareRequest(ACLMessage msg) {
			//uso un concepto propio de la ontolog�a de mobilidad
			//de JADE que me permite preguntar donde est� un agente.
			//El concepto es un tipo de Acci�n, que pide que se me informe
			//donde est� el agente especificado.
			WhereIsAgentAction whereIs = new WhereIsAgentAction();
			whereIs.setAgentIdentifier(destination);
			Action action = new Action(myAgent.getAMS(), whereIs);
			
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			String id = String.valueOf(System.currentTimeMillis());
			request.setConversationId(id);
			request.setLanguage(new SLCodec().getName());
			//uso la ontolog�a de mobilidad de JADE.
			request.setOntology(MobilityOntology.getInstance().getName());
			
			try {
				myAgent.getContentManager().fillContent(request, action);
			} catch (CodecException e) {
				throw new IllegalStateException ("Codec exception: " + e.getMessage());
			} catch (OntologyException e) {
				throw new IllegalStateException ("Ontology exception: " + e.getMessage());
			}
			request.addReceiver(myAgent.getAMS());
			System.out.println(myAgent.getName() + " buscando contenedor de " + destination);
			return request;
		}
		
		@Override
		protected void handleInform(ACLMessage inform) {
			Location location;
			try {
				//Obtengo el resultado de la acci�n de averiguar la 
				//localizaci�n del agente, que como era de suponer incluye
				//la localizaci�n del agente.
				ContentElement ce = myAgent.getContentManager().extractContent(inform);
				Result result = (Result) ce;
				location = (Location) result.getValue();
			} catch (UngroundedException e) {
				throw new IllegalStateException ("Ungrounded exception: " + e.getMessage());
			} catch (CodecException e) {			
				throw new IllegalStateException ("Codec exception: " + e.getMessage());
			} catch (OntologyException e) {
				throw new IllegalStateException ("Ontology exception: " + e.getMessage());					
			}					
						
			System.out.println(myAgent.getName() + " moviendome a " + location);
			//me muevo a esa localizaci�n.
			myAgent.doMove(location);
		}			
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
			reset();
			block(5000);
		}
		
		@Override
		protected void handleInform(ACLMessage inform) {
			ContentElement ce = null;
			try {
				ce = myAgent.getContentManager().extractContent(inform);
				if (Provides.class.isInstance(ce)) {
					System.out.println (myAgent.getName() + ": Encontr� el paper!");
					myAgent.addBehaviour(new MoveToAgent(myAgent, Provides.class.cast(ce).getProvider()));
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
			query.setOntology(PaperOntologyVocabulary.ONTOLOGY_NAME);
			query.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
			
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
	
	/**
	 * Se ejecuta luego de que el agente se movi� e imprime un mensaje por 
	 * consola.
	 */
	@Override
	protected void afterMove() {
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(PaperOntology.getInstance());
		getContentManager().registerOntology(MobilityOntology.getInstance());
		
		try {
			System.out.println(getName() + ": Llegu� a " + getContainerController().getContainerName());
		} catch (ControllerException e) {
			e.printStackTrace();
			System.out.println("Got to a container that refuses to tell me its name!");
		}
	}	
}
