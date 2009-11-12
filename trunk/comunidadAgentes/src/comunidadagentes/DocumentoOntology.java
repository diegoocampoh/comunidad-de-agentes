package comunidadagentes;
import jade.content.lang.sl.SL0Vocabulary;
//import jade.content.lang.sl.SL1Vocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
//import jade.content.schema.ObjectSchema;
//import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;


public final class DocumentoOntology extends Ontology implements DocumentoInterfazVocabulario{
	private static final DocumentoOntology instance = new DocumentoOntology();
	public static DocumentoOntology getInstance () {
		return instance;
	}
	
	public DocumentoOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {			
			//define un nuevo concepto
			add(new ConceptSchema(DOCUMENTO), Documento.class);
			//add(new PredicateSchema(PROVEE), Provee.class);
			
			ConceptSchema documento = (ConceptSchema) getSchema(DOCUMENTO);
			
			//agrega un atributo de tipo string requerido
			documento.add(TITULO, 
					(PrimitiveSchema) getSchema(BasicOntology.STRING));
			documento.add(AUTOR, 
					(PrimitiveSchema) getSchema(BasicOntology.STRING));
                       	documento.add(CONTENIDO, 
					(PrimitiveSchema) getSchema(BasicOntology.STRING));
			
			/* PROVEEDOR:
			PredicateSchema provides = (PredicateSchema) getSchema (PROVIDES);
			provides.add(PROVIDES_PROVIDER, getSchema(SL0Vocabulary.AID));
			provides.add(PROVIDES_PAPER, getSchema(PAPER));
			
			
			// Defino el operador NOT de SL1 para que se utilice la clase NOT en lugar
			// de un descriptor abstracto.
			add(new PredicateSchema(SL1Vocabulary.NOT), Not.class);
			PredicateSchema not = (PredicateSchema) getSchema(SL1Vocabulary.NOT);
		  	not.add(SL1Vocabulary.NOT_WHAT, PredicateSchema.getBaseSchema());
			*/
		} catch (OntologyException e) {
			throw new IllegalStateException ("Invalid ontology definition");
		}
	}
	
}
