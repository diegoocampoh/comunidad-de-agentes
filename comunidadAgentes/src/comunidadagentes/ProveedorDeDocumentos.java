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

    List<String> papers=new ArrayList<String>();

    private List<String> busqueda(String palabra)
    {
        List<String> resultado=new ArrayList<String>();
        for(String paper:papers)
        {
            if(paper.contains(palabra))
            {
                resultado.add(paper);
            }
        }
        return resultado;
    }

    public String puntaje(String palabra)
    {
        List<String> resultado=busqueda(palabra);
        return resultado.size()+"";
    }

    protected void setup(){
        System.out.println("Inicializando servicios.");

        papers.add("gozando con java");
        papers.add("gozando 2.0");
        papers.add("La programacion caótica");
        papers.add("Ing. Alejandro Roverano");
        ServiceDescription serv= new ServiceDescription();
        serv.setName("Busqueda de papers");
        serv.setType("Papers");
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
            respuesta.setContent(puntaje(cfp.getContent()));
            return respuesta;
        }

        @Override
        protected ACLMessage handleCfp(ACLMessage arg0) throws RefuseException, FailureException, NotUnderstoodException {
            System.out.println("Peticion recibida 1");
            return super.handleCfp(arg0);
        }


        @Override
        protected ACLMessage prepareResultNotification(ACLMessage arg0, ACLMessage arg1, ACLMessage arg2) throws FailureException {
            return super.prepareResultNotification(arg0, arg1, arg2);
        }



    
    }
}
