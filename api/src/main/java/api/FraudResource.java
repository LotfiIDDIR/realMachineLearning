package fr.ensma.lias.bimedia2018machinelearning.api;

import static fr.ensma.lias.bimedia2018machinelearning.api.ApiPaths.FRAUDS;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.ensma.lias.bimedia2018machinelearning.dto.FraudDTO;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
@Path(FRAUDS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface FraudResource {

	@GET
	List<FraudDTO> getFrauds();
}
