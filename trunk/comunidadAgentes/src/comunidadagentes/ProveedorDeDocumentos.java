package comunidadagentes;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.*;
import jade.lang.acl.*;
import jade.proto.ContractNetResponder;

import jade.util.leap.ArrayList;
import jade.util.leap.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vidal, ocampo aiello
 */
public class ProveedorDeDocumentos extends Agent {


    List papers=new ArrayList();
    List resultadoUltimaBusqueda = new ArrayList();

    public List getResultadoUltimaBusqueda() {
        return resultadoUltimaBusqueda;
    }

    public void setResultadoUltimaBusqueda(List resultadoUltimaBusqueda) {
        this.resultadoUltimaBusqueda = resultadoUltimaBusqueda;
    }

    String categoria;


    /**
     * Dadas las categorias pasadas por parámetro, retorna todos aquellos
     * documentos en los cuales se encuentran los papers.
     * @param keywords - Lista de palabras ingresadas en la consulta
     * @return Lista de papers en los que estan las keywords
     */
    private List busqueda(List keywords)
    {
        List resultado=new ArrayList();

        for (int i = 0; i < papers.size(); i++) {
            Documento paper=(Documento) papers.get(i);

             for (int j = 0; j < keywords.size(); j++) {
                String keyword=(String) keywords.get(j);

                 if(paper.esCategoria(keyword))
                {
                    resultado.add(paper);
                }

            }

        }
      
        return resultado;
    }

    /**
     *
     * @param palabra
     * @return
     */
    public String puntaje(List palabra)
    {   
        int puntajeMaximo = 0;
        int puntajeAcumulado;
        List resultadosOrdenados = new ArrayList();
        java.util.List<Puntaje> listaPuntajeada = new java.util.ArrayList<Puntaje>();
       
        for (int i = 0; i < papers.size(); i++) {
            Documento doc = (Documento)papers.get(i);

            puntajeAcumulado=0;
            for (int j = 0; j < palabra.size();j++) {
                String palabraObtenida =(String) palabra.get(j);

                if (doc.get(palabraObtenida)==null)
                {
                    continue;
                }

                puntajeAcumulado+=doc.get(palabraObtenida);
                
                if(puntajeAcumulado>puntajeMaximo)
                    puntajeMaximo=puntajeAcumulado;

            }

            if (puntajeAcumulado != 0){
                listaPuntajeada.add(new Puntaje(puntajeAcumulado, doc));
            }
            
        }

        Collections.sort(listaPuntajeada);
        for (Puntaje puntaje : listaPuntajeada) {
            resultadoUltimaBusqueda.add(puntaje.getDoc());

        }

        return String.valueOf(puntajeMaximo);

    }
    
    public ProveedorDeDocumentos(String categoria)
    {
        this.categoria=categoria;

    }

    public void agregarDocumento(Documento nuevoDocumento)
    {
        papers.add(nuevoDocumento);
    }


    @Override
    protected void setup(){
        getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(DocumentoOntology.getInstance());

        System.out.println("Inicializando servicios.");
        ServiceDescription serv= new ServiceDescription();
        serv.setName("Busqueda de papers");
        serv.setType(categoria);
        DFAgentDescription descrip = new DFAgentDescription();
        descrip.setName(getAID());
        descrip.addServices(serv);
        
        try {
            DFService.register(this, descrip);

        } catch (FIPAException exception) {
            exception.printStackTrace();
        }
        MessageTemplate plantilla = ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

        MessageTemplate template = MessageTemplate.and(
                                        MessageTemplate.MatchOntology(DocumentoOntology.getInstance().getName()),
                                        MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                                        MessageTemplate.MatchLanguage(new SLCodec().getName())));

        this.addBehaviour(new DevolverPaper(this,template));
    }

    private int calcularPuntaje(Documento doc, List palabra) {


        return 0;
    }
    
    private class DevolverPaper extends ContractNetResponder {
        public DevolverPaper(Agent agente, MessageTemplate plantilla) {
                super(agente, plantilla);
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage cfp) throws NotUnderstoodException, RefuseException {

            getResultadoUltimaBusqueda().clear();

            System.out.println("Peticion recibida 2");
            ACLMessage respuesta=cfp.createReply();
            respuesta.setPerformative(ACLMessage.PROPOSE);

            ContentElement ce = null;
			try {
				ce = myAgent.getContentManager().extractContent(cfp);
			} catch (CodecException e1) {
				e1.printStackTrace();
			} catch (OntologyException e1) {
				e1.printStackTrace();
			}

			if (ce == null || !Proveer.class.isInstance(ce)) {
				System.out.println (myAgent.getName() + ": Mensaje no entendido "+cfp.getContent());
                ACLMessage reply = cfp.createReply();
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                myAgent.send(reply);
				return reply;
			}

			Proveer query = (Proveer) ce;
            String puntaje=puntaje(query.getKeywords());

            if(puntaje.equals("0"))
            {
                //El puntaje es 0, por lo tanto, no tengo documentos para ofrecer.

                System.out.printf("Proveedor: No tengo nada que ofertar ", this.myAgent.getLocalName());
                ACLMessage refuse = cfp.createReply();
                refuse.setPerformative(ACLMessage.REFUSE);
                return refuse;
//                throw new RefuseException("No puedo crear propuesta.");

            }
            respuesta.setContent(String.valueOf(puntaje));
            return respuesta;
        }




        @Override
        protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
    
            ACLMessage reply=accept.createReply();
            reply.setPerformative(ACLMessage.INFORM);

            Resultado aEnviar = new Resultado(resultadoUltimaBusqueda);
     
            try {
                myAgent.getContentManager().fillContent(reply, aEnviar);
            } catch (CodecException ex) {
                Logger.getLogger(ProveedorDeDocumentos.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(ProveedorDeDocumentos.class.getName()).log(Level.SEVERE, null, ex);
            }

            return reply;
        }

    


        
   
    }
}
