package JadeExamples.src.uy.edu.ucu.agents.example.ontology;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

import java.util.Date;

/**
 * Define el concepto Paper, que se compone de un t�tulo, los autores del paper
 * y la fecha de publicaci�n.
 * @author saguiar
 *
 */
public final class Paper implements Concept {

	private String title;	
	private List authors;
	private Date publicationDate;
	
	/**
	 * Required no argument constructor for serialization
	 */ 	
	public Paper() {
		super();		
	}	
	
	/**
	 * Crea un nuevo paper con los datos especificados.
	 * @param title el t�tulo del paper.
	 * @param authors los autores del paper.
	 * @param publicationDate la fecha de publicaci�n del paper.
	 */
	public Paper(String title, Author[] authors, Date publicationDate) {
		this.title = title;
		
		this.authors = new ArrayList(authors.length);
		for (int i = 0; i < authors.length; i++) {
			this.authors.add(authors[i]);
		}
		
		this.publicationDate = publicationDate;
	}	
	public List getAuthors() {
		return authors;
	}
	public void setAuthors(List authors) {
		this.authors = authors;
	}
	public Date getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
