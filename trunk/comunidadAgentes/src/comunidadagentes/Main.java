package comunidadagentes;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.ArrayList;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
public class Main {

    /**
     * @param args the command line arguments
     */
	public static void main (String[] args) {

		//Perfil de ejecuci�n de jade para nuestro contenedor.
		Profile profile = new ProfileImpl(true); //contenedor primario (primary == true)

		// nombre del contenedor
		profile.setParameter(Profile.MASTER_NODE_NAME, "MainNode");

		profile.setParameter(Profile.CONTAINER_NAME, "PrimaryNode");

		// no configurar message transport protocols
		profile.setSpecifiers(Profile.MTPS, new ArrayList(0));

		//Cerrar la m�quina virtual cuando se cierra el �ltimo contenedor de Jade
		Runtime.instance().setCloseVM(true);

		AgentContainer container = Runtime.instance().createMainContainer(profile);

		try {
			container.start();

			//crear un nuevo agente en el contenedor e iniciarlo
			AgentController autos = container.acceptNewAgent("Automotora",new Autos ());
            autos.start();
			/*AgentController paper=container.acceptNewAgent("Paper", new Paper());
            paper.start();*/
            AgentController cliente = container.acceptNewAgent("Cliente",new Cliente ());
			cliente.start();
            /*AgentController usuario=container.acceptNewAgent("Usuario", new Usuario());
            usuario.start();*/

		} catch (StaleProxyException e) {
			e.printStackTrace();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

}
