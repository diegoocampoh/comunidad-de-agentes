package JadeExamples.src.uy.edu.ucu.agents.example.ontology;

import jade.content.lang.sl.SL0Vocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

/**
 * Define la ontolog�a usada para agentes que trabajan sobre papers.
 * @author saguiar
 *
 */
public final class PaperOntology extends Ontology implements PaperOntologyVocabulary {
	
	private static final PaperOntology instance = new PaperOntology();
	
	public static PaperOntology getInstance () {
		return instance;
	}
	
	public PaperOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {			
			//define los conceptos b�sicos de la ontolog�a
			add(new ConceptSchema(PAPER), Paper.class);
			add(new ConceptSchema(AUTHOR), Author.class);
			
			//define el predicado "Provee" que es cierto cuando un agente
			//provee un paper.
			add(new PredicateSchema(PROVIDES), Provides.class);
			
			ConceptSchema paper = (ConceptSchema) getSchema(PAPER);
			
			//agrega un atributo de tipo string requerido
			paper.add(PAPER_TITLE, 
					(PrimitiveSchema) getSchema(BasicOntology.STRING));
			
			//agrega un atributo de tipo date opcional
			paper.add(PAPER_PUBLICATION_DATE, 
					(PrimitiveSchema) getSchema(BasicOntology.DATE), 
					ObjectSchema.OPTIONAL);
			
			//agrega un atributo de tipo Author que debe contener de 1 a n 
			//elementos.
			paper.add(PAPER_AUTHORS, 
					(ConceptSchema) getSchema(AUTHOR), 
					1, ObjectSchema.UNLIMITED);
			
			ConceptSchema author = (ConceptSchema) getSchema(AUTHOR);
			author.add(AUTHOR_NAME, 
					(PrimitiveSchema) getSchema(BasicOntology.STRING));
			author.add(AUTHOR_INSTITUTE, 
					(PrimitiveSchema) getSchema(BasicOntology.STRING),
					ObjectSchema.OPTIONAL);
			
			PredicateSchema provides = (PredicateSchema) getSchema (PROVIDES);
			provides.add(PROVIDES_PROVIDER, getSchema(SL0Vocabulary.AID));
			provides.add(PROVIDES_PAPER, getSchema(PAPER));
			
		} catch (OntologyException e) {
			throw new IllegalStateException ("Invalid ontology definition");
		}
	}
	
}
