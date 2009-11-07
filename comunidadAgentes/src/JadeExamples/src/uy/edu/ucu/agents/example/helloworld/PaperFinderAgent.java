package JadeExamples.src.uy.edu.ucu.agents.example.helloworld;

import jade.core.Agent;

/**
 * Agente poco �til.
 * @author saguiar
 */
public class PaperFinderAgent extends Agent {
	/**
	 * El m�todo <code>setup</code> es el m�todo invocado por el AMS 
	 * (Agent Management System) cuando el agente es iniciado.
	 */
	@Override
	protected void setup() {
		super.setup();
		System.out.println ("Agent " + getName() + " is ready to serve you");
	}
}
