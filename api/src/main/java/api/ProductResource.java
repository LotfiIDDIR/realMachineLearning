package fr.ensma.lias.bimedia2018machinelearning.api;

import static fr.ensma.lias.bimedia2018machinelearning.api.ApiPaths.PRODUCTS;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.ensma.lias.bimedia2018machinelearning.dto.ProductDTO;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
@Path(PRODUCTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProductResource {

	@GET
	@Path("{" + ApiParameters.PDV + "}")
	List<ProductDTO> getProductsByPDV(@PathParam(ApiParameters.PDV) int pdv);

	@GET
	@Path(ApiPaths.SUGGEST + "/{" + ApiParameters.PDV + "}")
	List<ProductDTO> getSuggestedProductsByPDV(@PathParam(ApiParameters.PDV) int pdv,
			@QueryParam(ApiParameters.PRODUCT) final Set<String> list);
}
