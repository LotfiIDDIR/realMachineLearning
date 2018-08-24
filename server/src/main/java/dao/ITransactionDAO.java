package fr.ensma.lias.bimedia2018machinelearning.dao;

import java.util.Set;

import fr.ensma.lias.bimedia2018machinelearning.model.Transaction;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;
import fr.ensma.lias.bimedia2018machinelearning.prediction.Predictor;

/**
 * @author Lotfi IDDIR
 */
public interface ITransactionDAO {

	Set<Transaction> getTransactions(int limit);

	Set<Transaction> getTransactionsByDate(String date);

	Set<Transaction> getTransactionsByPDV(int PDV);

	Predictor getPredictor(String type);

	PredictionContext getPredictionContext(String type);

	void setFraud(Transaction fraude);
}
