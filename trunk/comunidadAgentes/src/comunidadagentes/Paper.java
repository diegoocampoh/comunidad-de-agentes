package comunidadagentes;
import jade.core.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.*;
import jade.lang.acl.*;
import jade.proto.ContractNetResponder;


/**
 *
 * @author vidal, ocampo aiello
 */
public class Paper extends Agent {
    protected void setup(){
        System.out.println("Inicializando servicios.");
        
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
            System.out.prinln("Peticion recibida", this.myAgent.getLocalName(), cfp.getSender().getLocalName());
        }

    
    }
}
