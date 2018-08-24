package fr.ensma.lias.bimedia2018machinelearning.services;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.api.TransactionResource;
import fr.ensma.lias.bimedia2018machinelearning.dao.ITransactionDAO;
import fr.ensma.lias.bimedia2018machinelearning.dto.TransactionDTO;
import fr.ensma.lias.bimedia2018machinelearning.model.DTOFactory;
import fr.ensma.lias.bimedia2018machinelearning.model.Transaction;

/**
 * @author Lotfi IDDIR
 */
public class TransactionResourceImpl implements TransactionResource {

	@Inject
	ITransactionDAO refTransactionDAO;

	@Override
	public List<TransactionDTO> getTransactions(int limit) {
		final Set<Transaction> transactions = refTransactionDAO.getTransactions(limit);
		
		return DTOFactory.createTransactionsDTO(transactions);
	}

	@Override
	public List<TransactionDTO> getTransactionsByDate(String date) {
		final Set<Transaction> transactions = refTransactionDAO.getTransactions(0);
		
		return DTOFactory.createTransactionsDTO(transactions);
	}

}
