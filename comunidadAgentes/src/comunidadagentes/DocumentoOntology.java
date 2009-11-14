package comunidadagentes;
//import jade.content.lang.sl.SL1Vocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
//import jade.content.schema.ObjectSchema;
//import jade.content.schema.PredicateSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;


public final class DocumentoOntology extends Ontology implements DocumentoInterfazVocabulario{

    /**
     * Patron sigleton
     */
    private static DocumentoOntology _instance;
    public static DocumentoOntology getInstance() {

        if (_instance == null)
            _instance = new DocumentoOntology();
        return _instance;
    }
	
	private DocumentoOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {			
			//define un nuevo concepto
			add(new ConceptSchema(DOCUMENTO), Documento.class);
            add(new PredicateSchema(RESULTADO), Resultado.class);

            add(new PredicateSchema(PROVEER), Proveer.class);
			//add(new PredicateSchema(PROVEE), Provee.class);
			
			ConceptSchema documento = (ConceptSchema) getSchema(DOCUMENTO);
			
			//agrega un atributo de tipo string requerido
			documento.add(DOCUMENTO_TITULO,
					(PrimitiveSchema) getSchema(BasicOntology.STRING));
			documento.add(DOCUMENTO_AUTOR,
					(PrimitiveSchema) getSchema(BasicOntology.STRING));
            documento.add(DOCUMENTO_CONTENIDO,
					(PrimitiveSchema) getSchema(BasicOntology.STRING));

			
			
			PredicateSchema proveer = (PredicateSchema) getSchema (PROVEER);
		
            proveer.add(PROVEER_KEYWORDS,
					(PrimitiveSchema) getSchema(BasicOntology.STRING),
					1, ObjectSchema.UNLIMITED);

            PredicateSchema resultado = (PredicateSchema) getSchema(RESULTADO);
            resultado.add(RESULTADO_DOCS, (ConceptSchema) getSchema(DOCUMENTO),
                    1, ObjectSchema.UNLIMITED);

			
			
		} catch (OntologyException e) {
			throw new IllegalStateException ("Invalid ontology definition");
		}
	}
	
}
