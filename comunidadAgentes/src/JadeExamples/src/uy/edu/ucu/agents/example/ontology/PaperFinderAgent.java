package JadeExamples.src.uy.edu.ucu.agents.example.ontology;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Agente que pregunta a otro agente si posee un paper determinado.
 * <p>
 * El agente utiliza una ontolog�a definida en {@link PaperOntology}.
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
		
		//Registrar la ontolog�a a usar por el agente, se utiliza en el 
		//behaviour
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(PaperOntology.getInstance());
		
		addBehaviour(new FindPaper(paper));
		
		System.out.println (getName() + ": Iniciado");
	}
	
	private static final class FindPaper extends Behaviour {		
				
		private final Paper paper;
		
		private boolean receivedResponse;
		private boolean sentQuery;
		
		FindPaper (Paper paper) {
			this.paper = paper;			
		}
		
		@Override		
		public void action() {
			if (!sentQuery) {
				
				AID provider = new AID("PaperProvider", false);
				
				System.out.println (myAgent.getName() + 
						": Buscando paper '" + paper.getTitle() + "'");
				
				sendQueryProvides (provider, paper);
				
				sentQuery = true;
				
			} else {
				//Se esperan mensajes que coincidan con �ste template.
				MessageTemplate template = 
					MessageTemplate.and(
							MessageTemplate.MatchPerformative(ACLMessage.INFORM),
							MessageTemplate.and(
									MessageTemplate.MatchOntology(PaperOntology.getInstance().getName()),
									MessageTemplate.MatchLanguage(new SLCodec().getName())));
				
				System.out.println (myAgent.getName() + ": Esperando respuesta.");
				
				//esperar 1 segundo por respuestas
				ACLMessage response = myAgent.blockingReceive(template, 1000);				
				
				receivedResponse = true;
				if (response == null) {
					System.out.println (myAgent.getName() + ": No encontr� el paper!");					
				} else {
					System.out.println (myAgent.getName() + ": Encontr� el paper!");
				}													
			}
		}
		
		@Override
		public boolean done() {
			return receivedResponse;			
		}
		
		private void sendQueryProvides (AID provider, Paper paper) {
			ACLMessage query = new ACLMessage(ACLMessage.QUERY_IF);
			query.addReceiver(provider);
			query.setOntology(PaperOntology.getInstance().getName());
			query.setLanguage(new SLCodec().getName());
			
			Provides publishes = new Provides(provider, paper);
			
			try {
				//en base a la ontolog�a del mensaje, el manejador de contenidos
				//carga el contenido del mensaje con la representaci�n en el 
				//lenguaje del mensaje del objeto provides.
				myAgent.getContentManager().fillContent(query, publishes);
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}					
			
			myAgent.send(query);
		}
	}
	
}
