package JadeExamples.src.uy.edu.ucu.agents.example.message;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Agente que averigua si otro agente conocido provee un cierto paper en base
 * al t�tulo del mismo.
 * <p>
 * El agente utiliza una ontolog�a sencilla. Los mensajes <code>QUERY_IF</code>
 * tienen como contenido el t�tulo del paper a buscar. Los <code>INFORM</code>,
 * enviados como respuesta a la pregunta, contienen "si" si el agente proveedor
 * provee un paper con ese t�tulo o "no" en caso contrario.
 * </p>
 * @author saguiar
 *
 */
public final class PaperFinderAgent extends Agent {

	/**
	 * El t�tulo del paper buscado.
	 */
	private final String paperTitle;
		
	public PaperFinderAgent(String paperTitle) {
		this.paperTitle = paperTitle;
	}

	@Override
	protected void setup() {
		super.setup();		
		System.out.println (getName() + ": cre�ndose!");
		addBehaviour(new FindPaper(paperTitle));
	}

	/**
	 * Un comportamiento que pregunta a otro agente si provee un paper con 
	 * un cierto t�tulo.
	 * <p>
	 * Una vez enviada la pregunta se espera indefinidamente por una respuesta.
	 * </p>
	 * @author saguiar
	 *
	 */
	private static final class FindPaper extends Behaviour {
		/**
		 * El t�tulo del paper buscado.
		 */
		private final String paperTitle;
		
		/**
		 * Verdadero si ya se envi� la pregunta.
		 */
		private boolean sentQuery;
		/**
		 * Verdadero si ya se recibi� la respuesta.
		 */
		private boolean receivedResponse;
		
		FindPaper (String paperTitle) {
			this.paperTitle = paperTitle;
			this.sentQuery = false;
		}
		
		@Override		
		public void action() {
			 
			if (!sentQuery) {
				// Aun no pregunt� si tiene el paper, por lo tanto, pregunto	
				ACLMessage query = new ACLMessage(ACLMessage.QUERY_IF);
				//mi destinatario es el paper provider.
				query.addReceiver(new AID("PaperProvider", false));
				//lo que estoy buscando es el titulo del paper
				query.setContent(paperTitle);
				
				//myAgent es una variable que contiene el agente que inici�
				//este comportamiento (en este caso una instancia de 
				//PaperFinderAgent)
				System.out.println (myAgent.getName() + 
						": Buscando el paper: " + paperTitle);
				
				//buscar!
				myAgent.send(query);
				sentQuery = true;
				
			} else {
				//Ya envi� la pregunta, esperar a que me informen la respuesta
				MessageTemplate template = MessageTemplate
					.MatchPerformative(ACLMessage.INFORM);
				
				ACLMessage response = myAgent.receive(template);
				if (response != null) {
					//recib� una respuesta!
					receivedResponse = true;
					if ("no".equals(response.getContent())) {
						System.out.println (myAgent.getName() + 
								": No encontr� el paper!");	
					} else {
						System.out.println (myAgent.getName() + 
								": Encontr� el paper!");
					}									
				} else {
					//no recib� respuesta, bloquearse hasta que llegue algo
					block();
				}
			}
		}
		
		@Override
		public boolean done() {
			//termin� cuando recib� la respuesta
			return receivedResponse;			
		}
	}	
}
