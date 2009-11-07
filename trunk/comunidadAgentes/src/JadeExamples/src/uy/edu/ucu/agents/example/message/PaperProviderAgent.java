package JadeExamples.src.uy.edu.ucu.agents.example.message;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
/**
 * Agente que averigua si otro agente conocido provee un cierto paper en base
 * al t�tulo del mismo.
 * 
 * @see PaperFinderAgent
 */
public class PaperProviderAgent extends Agent {
	
	/**
	 * Los t�tulos de los papers provistos por este agente.
	 */
	private final String[] paperTitles;
	
	public PaperProviderAgent(String[] paperTitles) {
		this.paperTitles = paperTitles;
	}

	@Override
	protected void setup() {
		super.setup();
		System.out.println (getName() + ": cre�ndose!");
		addBehaviour(new PublishPaper (paperTitles));
	}
	
	/**
	 * Un comportamiento que espera preguntas por un paper en particular e
	 * informa si el agente provee o no un paper con ese t�tulo.
	 * <p>
	 * El agente responde una pregunta por vez. 
	 * </p>
	 * @author saguiar
	 *
	 */
	private static final class PublishPaper extends Behaviour {

		private final String[] paperTitles;				
		
		public PublishPaper(String[] paperTitles) {
			this.paperTitles = paperTitles;
		}

		@Override
		public void action() {
			System.out.println(myAgent.getName() + 
					": Esperando b�squedas de papers");
			
			MessageTemplate template = MessageTemplate.MatchPerformative(
					ACLMessage.QUERY_IF);
			
			//espero una pregunta (QUERY_IF)
			ACLMessage message = myAgent.receive(template);
			if (message != null) {							
				//el contenido del mensaje deberia ser el titulo del paper
				//buscado
				String requestedPaperTitle = message.getContent();
				if (requestedPaperTitle != null) {
					ACLMessage reply = message.createReply();
					
					System.out.println (myAgent.getName() + 
							": Est�n buscando el paper: " + requestedPaperTitle);
					reply.setPerformative(ACLMessage.INFORM);
					//proveo el paper con ese t�tulo?
					if (providesPaper (requestedPaperTitle)){
						System.out.println (myAgent.getName() + 
								": Tengo el paper");
						reply.setContent("si");	
					} else {
						System.out.println (myAgent.getName() + 
								": No tengo el paper");
						reply.setContent("no");
					}
					
					myAgent.send(reply);
				} 							
			} else {
				//no hab�a respuestas, me bloqueo hasta que llegue una
				block();
			}
		}

		@Override
		public boolean done() {
			//no termino nunca, siempre quedo esperando por futuras preguntas.
			return false;
		}
		
		private boolean providesPaper (String requestedPaperTitle) {
			for (String providedPaperTitle: paperTitles) {
				if (requestedPaperTitle.equals (providedPaperTitle)) {
					return true;
				}
			}	
			return false;
		}
		
	}
	
}
