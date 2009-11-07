package JadeExamples.src.uy.edu.ucu.agents.example.helloworld;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.ArrayList;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class Main {

	/**
	 * Punto de entrada.
	 * @param args lista de argumentos pasados por par�metros, se ignoran.
	 */
	public static void main (String[] args) {				
		
		//Perfil de ejecuci�n de jade para nuestro contenedor.
		Profile profile = new ProfileImpl(true); //contenedor primario (primary == true)

		// nombre del contenedor
		profile.setParameter(Profile.MASTER_NODE_NAME, "MainNode");
		
		profile.setParameter(Profile.CONTAINER_NAME, "PrimaryNode");
		
		// no configurar message transport protocols
		profile.setSpecifiers(Profile.MTPS, new ArrayList(0)); 
		
		//Cerrar la m�quina virtual cuando se cierra el �ltimo contenedor de Jade
		Runtime.instance().setCloseVM(true);
		
		AgentContainer container = Runtime.instance().createMainContainer(profile);
		
		try {
			container.start();
			
			//crear un nuevo agente en el contenedor e iniciarlo
			AgentController finder = container.acceptNewAgent(
					"PaperFinder-1", 
					new PaperFinderAgent ());
			finder.start();					
			
		} catch (StaleProxyException e) {
			e.printStackTrace();			
		} catch (ControllerException e) {
			e.printStackTrace();
		}		
	}	
}
