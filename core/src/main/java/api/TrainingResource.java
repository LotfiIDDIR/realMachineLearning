package fr.ensma.lias.bimedia2018machinelearning.api;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import fr.ensma.lias.bimedia2018machinelearning.dto.MetricsDTO;

/**
 * @author Lotfi IDDIR
 */
@Path("/train")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)

public interface TrainingResource {
	
	@POST
	@Path("/upload")
	void upload(@FormDataParam("file") InputStream uploaded, @FormDataParam("file") FormDataContentDisposition fileDetail);
	
	@GET
	@Path("/{product}/{algo}")
	MetricsDTO getMetrics(@PathParam("product")String product,@PathParam("algo")String model,
			@DefaultValue("0.5")@QueryParam("fraud")double fraudPortion,@DefaultValue("0.6")@QueryParam("training")double trainingPortion,
			@DefaultValue("3")@QueryParam("nb")int nb,@DefaultValue("5")@QueryParam("interval")int interval,@DefaultValue("3")@QueryParam("num")int numTree);
}
