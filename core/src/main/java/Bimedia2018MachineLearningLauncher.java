package fr.ensma.lias.bimedia2018machinelearning;

import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.weld.environment.se.Weld;

import fr.ensma.lias.bimedia2018machinelearning.service.TrainingResourceImpl;

/**
 * @author Lotfi IDDIR
 */
public class Bimedia2018MachineLearningLauncher {

	public static final URI BASE_URI = getBaseURI();

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://127.0.0.1/").port(9992).build();
	}

	public static void main(String[] args) {
		Logger l = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
		l.setLevel(Level.FINE);
		l.setUseParentHandlers(false);
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.ALL);
		l.addHandler(ch);

		ResourceConfig rc = new ResourceConfig();
		rc.registerClasses( TrainingResourceImpl.class, MultiPartFeature.class, CrossDomainFilter.class);
		rc.property("jersey.config.server.wadl.disableWadl", "false");

		Weld weld = new Weld();
		weld.initialize();
		try {
			HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
			server.start();

			System.out.println(String.format("Jersey app started available at %s", BASE_URI, BASE_URI));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}