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

    protected void setup()
    {
        DFAgentDescription descripcion=new DFAgentDescription();
        //descripcion.addLanguages("mafioso");
        ServiceDescription servicio=new ServiceDescription();
        descripcion.addServices(servicio);
        try
        {
            DFAgentDescription[] resultados=DFService.search(this, descripcion);
            if(resultados.length>0)
            {
                ACLMessage mensajeCFP = new ACLMessage(ACLMessage.CFP);
                for(DFAgentDescription agente : resultados)
                {
                    mensajeCFP.addReceiver(agente.getName());
                }
            }
            ACLMessage plantilla=new ACLMessage(ACLMessage.CFP);
            plantilla.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            this.addBehaviour(new Comportamiento(this, plantilla));
        }
        catch(Exception e)
        {
            System.out.println("se rompio todo");
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
