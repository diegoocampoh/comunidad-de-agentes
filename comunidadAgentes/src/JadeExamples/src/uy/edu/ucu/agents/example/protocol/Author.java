package JadeExamples.src.uy.edu.ucu.agents.example.protocol;

import jade.content.Concept;

/**
 * Define el concepto de Autor, que comprende el nombre del autor y el 
 * instituto a el cual pertenece.
 * @author saguiar
 *
 */
public final class Author implements Concept {
	private String name;
	private String institute;
	
	/**
	 * Required no argument constructor for serialization
	 */
	public Author() {
		super();
	}
	/**
	 * Define un autor que no pertenece a ning�n instituto.
	 * @param name el nombre del autor.
	 */
	public Author(String name) {
		this(name, null);
	}
	/**
	 * Define un autor que pertenence al instituto dado.
	 * @param name el nombre del autor.
	 * @param institute el instituto al cual pertenece el autor.
	 */
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
