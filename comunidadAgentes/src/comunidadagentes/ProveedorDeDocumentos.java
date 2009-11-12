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

/**
 *
 * @author vidal, ocampo aiello
 */
public class ProveedorDeDocumentos extends Agent {


    List papers=new ArrayList();
    String categoria;
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

    public String puntaje(List palabra)
    {
        List resultado=busqueda(palabra);
        return resultado.size()+"";
    }
    
    public ProveedorDeDocumentos(String categoria)
    {
        this.categoria=categoria;

    }

    public void agregarDocumento(Documento nuevoDocumento)
    {
        papers.add(nuevoDocumento);
    }


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
    
    private class DevolverPaper extends ContractNetResponder {
        public DevolverPaper(Agent agente, MessageTemplate plantilla) {
                super(agente, plantilla);
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
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
                throw new RefuseException("No puedo crear propuesta.");

            }
            respuesta.setContent(String.valueOf(puntaje));
            return respuesta;
        }




        @Override
        protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
            Proveer consulta=new Proveer();
            ACLMessage respuesta=accept.createReply();
            respuesta.setPerformative(ACLMessage.INFORM);
            try
            {
                consulta=(Proveer)cfp.getContentObject();
            }
            catch(UnreadableException e)
            {
                e.printStackTrace();
            }
            Resultado resultado=new Resultado(busqueda(consulta.getKeywords()));
            try
            {
                myAgent.getContentManager().fillContent(respuesta, resultado);
            }
            catch(OntologyException e)
            {
                e.printStackTrace();
            }
            catch(CodecException e)
            {
                e.printStackTrace();
            }
            return respuesta;
        }

        
   
    }
}
