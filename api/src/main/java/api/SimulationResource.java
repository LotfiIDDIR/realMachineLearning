package fr.ensma.lias.bimedia2018machinelearning.api;

import static fr.ensma.lias.bimedia2018machinelearning.api.ApiPaths.SIMULATION;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
@Path(SIMULATION)
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
