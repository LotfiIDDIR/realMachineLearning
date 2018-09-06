package fr.ensma.lias.bimedia2018machinelearning.api;

import static fr.ensma.lias.bimedia2018machinelearning.api.ApiPaths.TRANSACTIONS;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.ensma.lias.bimedia2018machinelearning.dto.TransactionDTO;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
@Path(TRANSACTIONS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TransactionResource {

	@GET
	List<TransactionDTO> getTransactions(@QueryParam("limit") int limit);

	@GET
	@Path("{date}")
	List<TransactionDTO> getTransactionsByDate(@PathParam("date") String date);
}
