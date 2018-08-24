package fr.ensma.lias.bimedia2018machinelearning.api;

import static fr.ensma.lias.bimedia2018machinelearning.api.ApiPaths.SIMULATION;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
@Path(SIMULATION)
@Produces(MediaType.APPLICATION_JSON)
public interface SimulationResource {

	@POST
	@Path(ApiPaths.START)
	void start(@QueryParam(ApiParameters.DURATION) int dayInMinutes);

	@POST
	@Path(ApiPaths.STOP)
	void stop();
	
	@GET
	@Path("/time")
	String getTime();
}
