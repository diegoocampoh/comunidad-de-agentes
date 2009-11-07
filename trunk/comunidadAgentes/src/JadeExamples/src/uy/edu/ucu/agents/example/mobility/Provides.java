package JadeExamples.src.uy.edu.ucu.agents.example.mobility;

import jade.content.Predicate;
import jade.core.AID;

public final class Provides implements Predicate {
	private AID provider;
	private Paper paper;
		
	/**
	 * Required no argument constructor for serialization
	 */
	public Provides() {
		super();		
	}
	
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
