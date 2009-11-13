package comunidadagentes;

/**
 *
 * @author Administrador
 */

import jade.content.lang.sl.SLCodec;
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
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Usuario extends Agent {


    @Override
    protected void setup()
    {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(DocumentoOntology.getInstance());
        //descripcion.addLanguages("mafioso");
        ServiceDescription servicio=new ServiceDescription();
        DFAgentDescription descripcion=new DFAgentDescription();
        descripcion.addServices(servicio);
        try
        {

            List frutas = new ArrayList();
            frutas.add("Fruteando");

            Proveer provee = new Proveer();
            provee.setKeywords(frutas);




            ACLMessage plantilla=new ACLMessage(ACLMessage.CFP);
            plantilla.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
           
            DFAgentDescription[] resultados=DFService.search(this, descripcion);
 
            if(resultados.length>0)
            {
                ACLMessage mensajeCFP = new ACLMessage(ACLMessage.CFP);
                mensajeCFP.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
             
                mensajeCFP.setReplyByDate(new Date(System.currentTimeMillis() + 15000));
                mensajeCFP.setOntology(DocumentoOntology.getInstance().getName());
                mensajeCFP.setLanguage(new SLCodec().getName());
                getContentManager().fillContent(mensajeCFP, provee);
                for(DFAgentDescription agente : resultados)
                {
                    mensajeCFP.addReceiver(agente.getName());
                }

                this.addBehaviour(new Comportamiento(this, mensajeCFP));
                
                //this.send(mensajeCFP);
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

                if (respuesta.getPerformative()==ACLMessage.REFUSE) break;

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
            List papers=informe.getDocumentos();
            
            for (int i = 0; i < papers.size(); i++) {
                Documento doc = (Documento)papers.get(i);
                System.out.println(doc.getTitulo());
            }
                
            
        }

        @Override
        protected void handleRefuse(ACLMessage refuse) {
            super.handleRefuse(refuse);
            System.out.println("Propuesta rechazada");
        }

     }

}
