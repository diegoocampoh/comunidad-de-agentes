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
import java.util.*;

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
        protected void handleAllResponses(Vector responses, Vector acceptances) {
            int mejorOferta=0;
            ACLMessage mensajeMejorOferta=null;
            for(Object respuestaObject:responses)
            {
                ACLMessage respuesta=(ACLMessage) respuestaObject;
                ACLMessage contraRespuesta=null;
                if(respuesta.getPerformative()==ACLMessage.PROPOSE)
                {
                    contraRespuesta=respuesta.createReply();
                    contraRespuesta.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    acceptances.add(contraRespuesta);
                }
                String ofertaString=respuesta.getContent();

                int oferta=Integer.getInteger(ofertaString);
                if(oferta>mejorOferta)
                {
                    mejorOferta=oferta;
                    mensajeMejorOferta=contraRespuesta;
                }
            }
            if(mensajeMejorOferta!=null)
            {
                mensajeMejorOferta.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            }
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            Resultado informe=null;
            try
            {
                informe=(Resultado) inform.getContentObject();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            List<Documento> papers=informe.getDocumentos();
            for(Documento doc:papers)
            {
                System.out.println(doc.getTitulo());
            }
        }
     }

}
