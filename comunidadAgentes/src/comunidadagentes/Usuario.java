/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunidadagentes;

/**
 *
 * @author Administrador
 */

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Date;
import java.util.Vector;

public class Usuario extends Agent {


    @Override
    protected void setup()
    {
        //descripcion.addLanguages("mafioso");
        ServiceDescription servicio=new ServiceDescription();
        DFAgentDescription descripcion=new DFAgentDescription();
        descripcion.addServices(servicio);
        try
        {
            ACLMessage plantilla=new ACLMessage(ACLMessage.CFP);
            plantilla.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            this.addBehaviour(new Comportamiento(this, plantilla));
            DFAgentDescription[] resultados=DFService.search(this, descripcion);
            if(resultados.length>0)
            {
                ACLMessage mensajeCFP = new ACLMessage(ACLMessage.CFP);
                mensajeCFP.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
                mensajeCFP.setContent("Dame esos libros loco!");
                mensajeCFP.setReplyByDate(new Date(System.currentTimeMillis() + 15000));
                for(DFAgentDescription agente : resultados)
                {
                    mensajeCFP.addReceiver(agente.getName());
                }
                this.send(mensajeCFP);
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

     private class Comportamiento extends ContractNetInitiator
     {

        public Comportamiento(Agent myAgent, ACLMessage plantilla )
        {
            super(myAgent, plantilla);
        }


        @Override
        protected void handlePropose(ACLMessage arg0, Vector arg1) {
            System.out.println(arg0.getContent());
        }
     }

}
