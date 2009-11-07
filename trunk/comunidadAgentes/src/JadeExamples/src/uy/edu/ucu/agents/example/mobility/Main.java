package JadeExamples.src.uy.edu.ucu.agents.example.mobility;

import java.util.Date;

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
							
		try {
			Profile profile = new ProfileImpl(true);			
			profile.setParameter(Profile.MASTER_NODE_NAME, "PrimaryNode");
			profile.setParameter(Profile.CONTAINER_NAME, "PrimaryNode");
			profile.setSpecifiers(Profile.MTPS, new ArrayList(0));
			
			Runtime.instance().setCloseVM(true);
			
			AgentContainer container = Runtime.instance().createMainContainer(profile);
			
			container.start();									
			
			AgentController finderNOk = container.acceptNewAgent(
					"PaperFinder-1", 
					new PaperFinderAgent (
							new Paper (
									"Hackers and Painters",
									new Author[] { new Author ("Paul Graham")},
									new Date())));
			finderNOk.start();
			
			AgentController finderOk = container.acceptNewAgent(
					"PaperFinder-2", 
					new PaperFinderAgent (
							new Paper (
									"On Formally Undecidable Propositions in Principia Mathematica and Related Systems I.",
									new Author[] { new Author ("Kurt God�l") },
									new Date())));
			finderOk.start();
			
			Profile secondary = new ProfileImpl (false);
			secondary.setParameter(Profile.MASTER_NODE_NAME, "MainNode");
			secondary.setParameter(Profile.CONTAINER_NAME, "SecondaryNode");
			secondary.setSpecifiers(Profile.MTPS, new ArrayList(0));
					
			AgentContainer secondaryContainer = Runtime.instance().createAgentContainer(secondary);
				
			secondaryContainer.start();
			AgentController provider = secondaryContainer.acceptNewAgent(
					"PaperProvider", 
					new PaperProviderAgent (new Paper[] {
							new Paper (
									"On Formally Undecidable Propositions in Principia Mathematica and Related Systems I.",
									new Author[] { new Author ("Kurt God�l") },
									new Date()
									),
							new Paper (
									"Agent Oriented Software Engineering",
									new Author[] { new Author ("Nicholas Jennings", "Michael Wooldridge")},
									new Date()
									)
							})						
			); 					
			provider.start();
			
		} catch (StaleProxyException e) {
			e.printStackTrace();			
		} catch (ControllerException e) {
			e.printStackTrace();
		}		
	}
	
}
