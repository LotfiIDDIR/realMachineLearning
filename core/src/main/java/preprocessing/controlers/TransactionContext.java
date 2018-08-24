package fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers;

import java.io.Serializable;

import org.apache.spark.mllib.linalg.Vector;

/**
 * @author  Lotfi IDDIR
 */

import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.generation.GenerTransaction;
import fr.ensma.lias.bimedia2018machinelearning.learning.Iclassification;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSV;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Transaction;

public class TransactionContext implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3992021396576368995L;
	private Transaction transact ;
	public TransactionContext(Transaction transact) {
		super();
		this.setTransact(transact);
	}
	public Transaction getTransact() {
		return transact;
	}
	public void setTransact(Transaction transact) {
		this.transact = transact;
	}
	public Iclassification convertToIclassification(LabeledPoint point)
	{
		return transact.convertToIclassification(point);
	}
	public LabeledPoint convertToLabeledPoint()
	{
		return transact.convertToLabeledPoint();
	}
	public LabeledPoint convertToLabeledPoint(String line,char separator)
	{
		return transact.convertToLabeledPoint(line,separator);
	}
	public String writeCSV(char separator)
	{
		return transact.writeCSV(separator);
	}
	public ICSV readCSV(String[] line,boolean treated)
	{
		return transact.readCSV(line, treated);
	}
	public ICSV getICSV() {
		return transact;
	}
	public Transaction initTransaction()
	{
		String path = transact.getVictimsPath();
		GenerTransaction.fillVictims(path, ',');
		return transact.randomInit(GenerTransaction.pdvVictimes);
	}
	public String writeFraudDetail(Vector v)
	{
		return transact.writeFraudDetails(v);
	}
	public int getFraudIndex()
	{
		return transact.getFraudIndex();
	}
	public Vector toVector(String line []) {
	    return transact.toVector(line);
	}
	public String writeLabeledPoint(LabeledPoint point, char separator) {
	    return transact.writeLabeledPoint(point, separator);
	}
}
