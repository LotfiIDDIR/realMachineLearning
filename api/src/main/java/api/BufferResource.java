package fr.ensma.lias.bimedia2018machinelearning.api;

import static fr.ensma.lias.bimedia2018machinelearning.api.ApiPaths.BUFFERS;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.ensma.lias.bimedia2018machinelearning.dto.BufferDTO;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
@Path(BUFFERS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BufferResource {

	@GET
	List<BufferDTO> getBuffers(@QueryParam(ApiParameters.LIMIT) int limit);
}
