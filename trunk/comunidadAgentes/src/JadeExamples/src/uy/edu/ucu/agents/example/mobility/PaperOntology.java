package JadeExamples.src.uy.edu.ucu.agents.example.mobility;

import jade.content.lang.sl.SL0Vocabulary;
import jade.content.lang.sl.SL1Vocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;


public final class PaperOntology extends Ontology implements PaperOntologyVocabulary {
	
	private static final PaperOntology instance = new PaperOntology();
	
	public static PaperOntology getInstance () {
		return instance;
	}
	
	public PaperOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {			
			//define un nuevo concepto
			add(new ConceptSchema(PAPER), Paper.class);
			add(new ConceptSchema(AUTHOR), Author.class);
			add(new PredicateSchema(PROVIDES), Provides.class);
			
			ConceptSchema paper = (ConceptSchema) getSchema(PAPER);
			
			//agrega un atributo de tipo string requerido
			paper.add(PAPER_TITLE, 
					(PrimitiveSchema) getSchema(BasicOntology.STRING));
			
			//agrega un atributo de tipo date opcional
			paper.add(PAPER_PUBLICATION_DATE, 
					(PrimitiveSchema) getSchema(BasicOntology.DATE), 
					ObjectSchema.OPTIONAL);
			
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
			
			
			// Defino el operador NOT de SL1 para que se utilice la clase NOT en lugar
			// de un descriptor abstracto.
			add(new PredicateSchema(SL1Vocabulary.NOT), Not.class);
			PredicateSchema not = (PredicateSchema) getSchema(SL1Vocabulary.NOT);
		  	not.add(SL1Vocabulary.NOT_WHAT, PredicateSchema.getBaseSchema());
			
		} catch (OntologyException e) {
			throw new IllegalStateException ("Invalid ontology definition");
		}
	}
	
}
