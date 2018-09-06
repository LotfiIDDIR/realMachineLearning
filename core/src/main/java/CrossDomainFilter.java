package fr.ensma.lias.bimedia2018machinelearning;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * @author Lotfi IDDIR
 */
@Provider
public class CrossDomainFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext cresp) throws IOException {
		cresp.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
		cresp.getHeaders().add("Access-Control-Allow-Credentials", "true");
		cresp.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		cresp.getHeaders().add("Allow", "GET, POST, DELETE, PUT");
		cresp.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Accept");
	}
}
