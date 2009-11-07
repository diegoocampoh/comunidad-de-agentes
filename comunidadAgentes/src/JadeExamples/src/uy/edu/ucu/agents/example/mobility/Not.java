package JadeExamples.src.uy.edu.ucu.agents.example.mobility;

import jade.content.Predicate;

public class Not implements Predicate {

	private Predicate what;

	public Not() {
		super();		
	}

	public Not(Predicate what) {		
		this.what = what;
	}

	public Predicate getWhat() {
		return what;
	}

	public void setWhat(Predicate what) {
		this.what = what;
	}
	
	
}
