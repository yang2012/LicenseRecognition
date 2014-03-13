package nju.edu.cn.restlet;

import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;

public class LRSystemComponent extends Component {
	/**
	 * Launches the server component.
	 * 
	 * @param args
	 *            The arguments.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new LRSystemComponent().start();
	}

	/**
	 * Constructor.
	 * 
	 * @throws Exception
	 */
	public LRSystemComponent() throws Exception {
		// Set basic properties
		setName("License Recognition Server");
		setDescription("License Recognition");
		setOwner("Software college");
		setAuthor("Justin Yang");

		// Add connectors
		getClients().add(new Client(Protocol.CLAP));
		getClients().add(new Client(Protocol.FILE));

		Server server = new Server(new Context(), Protocol.HTTP, 8183);
		
		// Tracing
		getServers().add(server);

		LRSystemApplication app = new LRSystemApplication();
		
		// Configure the default virtual host
		VirtualHost host = getDefaultHost();

		// Attach the application to the default virtual host
		host.attachDefault(app);
	}
}
