package fr.ensma.lias.bimedia2018machinelearning.dao.inmemory;

import java.util.Set;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.api.NotYetImplementedException;
import fr.ensma.lias.bimedia2018machinelearning.dao.ITransactionDAO;
import fr.ensma.lias.bimedia2018machinelearning.engine.Engine;
import fr.ensma.lias.bimedia2018machinelearning.model.Transaction;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;
import fr.ensma.lias.bimedia2018machinelearning.prediction.Predictor;

/**
 * @author Lotfi IDDIR
 */

public class TransactionMemoryDAO implements ITransactionDAO {

	@Inject
	Engine engine;

	@Override
	public Set<Transaction> getTransactions(int limit) {
		return engine.getTreatedtransactions(limit);
	}

	@Override
	public Set<Transaction> getTransactionsByDate(String date) {
		throw new NotYetImplementedException();
	}

	@Override
	public Set<Transaction> getTransactionsByPDV(int PDV) {
		throw new NotYetImplementedException();
	}

	@Override
	public Predictor getPredictor(String type) {
		throw new NotYetImplementedException();
	}

	@Override
	public PredictionContext getPredictionContext(String type) {
		throw new NotYetImplementedException();
	}

	@Override
	public void setFraud(Transaction fraude) {
		throw new NotYetImplementedException();
	}
}
