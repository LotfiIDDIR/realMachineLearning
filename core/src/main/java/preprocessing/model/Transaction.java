package fr.ensma.lias.bimedia2018machinelearning.preprocessing.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.learning.Iclassification;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSV;

/**
 * @author  Lotfi IDDIR
 */


// Il faut faire un refactoring

public abstract class Transaction implements ICSV,Iclassification,Serializable,Comparable<Transaction>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 729034947373112628L;
	protected double montant;
	protected int pdv;// le clicod du point de vente
	protected Timestamp dateDebut;
	protected int fraud;
	protected long dateNoTime;
	protected int pdvCluster;
	
	//Getters and Setters
	
	public int getFraud() {
	    return fraud;
	}
	public void setFraud(int fraud) {
	    this.fraud = fraud;
	}
		
	public double getMontant() {
	    return montant;
	}
	public void setMontant(double montant) {
	    this.montant = montant;
	}
	public int getPDV() {
	    return pdv;
	}
	public void setPDV(int pDV) {
	    this.pdv = pDV;
	}
	public Timestamp getDateDebut() {
	    return dateDebut;
	}
	public void setDateDebut(Timestamp dateDebut) {
	    this.dateDebut = dateDebut;
	}
	public long getDateNoTime() {
	    return dateNoTime;
	}
	public void setDateNoTime(long dateNoTime) {
	    this.dateNoTime = dateNoTime;
	}
	public int getPdvCluster() {
	    return pdvCluster;
	}
	public void setPdvCluster(int pdvCluster) {
	    this.pdvCluster = pdvCluster;
	}

	@Override
	public int compareTo(Transaction t) {
	    return this.getDateDebut().compareTo(t.getDateDebut());
	}	
	
	public abstract ICSV readCSV(String[] line,boolean treated);
	public abstract String writeCSV(char separator);
	public abstract LabeledPoint convertToLabeledPoint();
	public abstract LabeledPoint convertToLabeledPoint(String line,char separator);
	public abstract Iclassification convertToIclassification(LabeledPoint point);
	public abstract Transaction randomInit(List<String> pdvVictimes);
	public abstract String getVictimsPath();
	public abstract String writeFraudDetails(Vector v);
	public abstract int getFraudIndex();
	public abstract String toString();
	public abstract Vector toVector();
	public abstract Vector toVector(String line[]);
	public abstract String writeLabeledPoint(LabeledPoint point,char separator);
}
