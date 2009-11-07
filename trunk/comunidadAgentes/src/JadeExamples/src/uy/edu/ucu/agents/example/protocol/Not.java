package JadeExamples.src.uy.edu.ucu.agents.example.protocol;

import jade.content.Predicate;

/**
 * Un predicado que dice que otro predicado es falso. 
 * @author saguiar
 *
 */
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
