package JadeExamples.src.uy.edu.ucu.agents.example.mobility;

import jade.content.Concept;

public final class Author implements Concept {
	private String name;
	private String institute;
	
	/**
	 * Required no argument constructor for serialization
	 */
	public Author() {
		super();
	}
	public Author(String name) {
		this(name, null);
	}
	public Author(String name, String institute) {
		this.name = name;
		this.institute = institute;
	}
	public String getInstitute() {
		return institute;
	}
	public void setInstitute(String institute) {
		this.institute = institute;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}
