package JadeExamples.src.uy.edu.ucu.agents.example.protocol;

import jade.content.Predicate;
import jade.core.AID;

/**
 * Un predicado que es cierto cuando el agente provee el paper.
 * @author saguiar
 *
 */
public final class Provides implements Predicate {
	/**
	 * El agente que provee el paper.
	 */
	private AID provider;
	/**
	 * El paper provisto.
	 */
	private Paper paper;
		
	/**
	 * Required no argument constructor for serialization
	 */
	public Provides() {
		super();		
	}
	
	/**
	 * Define que el agente provee el paper.
	 * @param publisher el agente.
	 * @param paper el paper.
	 */
	public Provides(AID publisher, Paper paper) {		
		this.provider = publisher;
		this.paper = paper;
	}
	
	public Paper getPaper() {
		return paper;
	}
	
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
	
	public AID getProvider() {
		return provider;
	}
	
	public void setProvider(AID publisher) {
		this.provider = publisher;
	}	
}
