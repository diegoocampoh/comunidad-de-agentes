package comunidadagentes;
import jade.core.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.*;
import jade.lang.acl.*;
import jade.proto.ContractNetResponder;
import java.util.*;

/**
 *
 * @author vidal, ocampo aiello
 */
public class ProveedorDeDocumentos extends Agent {

    List<Documento> papers=new ArrayList<Documento>();
    String categoria;
    private List<Documento> busqueda(List<String> keywords)
    {
        List<Documento> resultado=new ArrayList<Documento>();
        for(Documento paper:papers)
        {
            for(String keyword:keywords)
            {
                if(paper.esCategoria(keyword))
                {
                    resultado.add(paper);
                }
            }
        }
        return resultado;
    }

    public String puntaje(List<String> palabra)
    {
        List<Documento> resultado=busqueda(palabra);
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
        this.addBehaviour(new DevolverPaper(this,plantilla));
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
            Proveer consulta=new Proveer();
            try
            {
                consulta=(Proveer)cfp.getContentObject();
            }
            catch(UnreadableException e)
            {
                e.printStackTrace();
            }
            
            respuesta.setContent(puntaje(consulta.getKeywords()));
            return respuesta;
        }




        @Override
        protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
            Proveer consulta=new Proveer();
            ACLMessage respuesta=accept.createReply();
            try
            {
                consulta=(Proveer)cfp.getContentObject();
            }
            catch(UnreadableException e)
            {
                e.printStackTrace();
            }
            respuesta.setContentObject();
        }
    
    }
}
