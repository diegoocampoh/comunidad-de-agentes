package JadeExamples.src.uy.edu.ucu.agents.example.message;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.ArrayList;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class Main {

	public static void main (String[] args) {				
		
		Profile profile = new ProfileImpl(true);
		profile.setParameter(Profile.MASTER_NODE_NAME, "MainNode");
		profile.setParameter(Profile.CONTAINER_NAME, "PrimaryNode");
		profile.setSpecifiers(Profile.MTPS, new ArrayList(0));
		
		Runtime.instance().setCloseVM(true);
		
		AgentContainer container = Runtime.instance().createMainContainer(profile);
		
		try {
			container.start();						
			
			//ahora iniciamos dos agentes en lugar de uno
			AgentController finder = container.acceptNewAgent(
					"PaperFinder-1", 
					new PaperFinderAgent ("A Survey of Multi-Agent Systems"));
			finder.start();
			
			AgentController provider = container.acceptNewAgent(
					"PaperProvider", 
					new PaperProviderAgent (
							new String[] { 
									"Another survey of Multi-Agent Systems"		
							})); 					
			provider.start();
			
		} catch (StaleProxyException e) {
			e.printStackTrace();			
		} catch (ControllerException e) {
			e.printStackTrace();
		}		
	}
	
}
