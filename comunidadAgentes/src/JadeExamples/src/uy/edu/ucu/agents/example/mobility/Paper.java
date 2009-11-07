package JadeExamples.src.uy.edu.ucu.agents.example.mobility;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

import java.util.Date;

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
