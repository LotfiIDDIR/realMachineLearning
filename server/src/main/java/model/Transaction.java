package fr.ensma.lias.bimedia2018machinelearning.model;

import java.sql.Timestamp;

import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class Transaction implements Comparable<Transaction> {

	private double montant;

	private int pdv;// le clicod du point de vente

	private Timestamp dateDebut;

	private int quantity;

	private int fraud;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Timestamp getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Timestamp dateDebut) {
		this.dateDebut = dateDebut;
	}

	public int getPdv() {
		return pdv;
	}

	public void setPdv(int pdv) {
		this.pdv = pdv;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	@Override
	public int compareTo(Transaction t) {
		return this.getDateDebut().compareTo(t.getDateDebut());
	}

	public int getFraud() {
		return fraud;
	}

	public void setFraud(int fraud) {
		this.fraud = fraud;
	}

	public Vector toVector() {
		double[] features = { pdv, dateDebut.getTime() % 86400000, quantity, montant };
		Vector vector = new DenseVector(features);
		return vector;
	}

	public String toString() {
		String result = "";
		result += "PDV : " + this.getPdv() + "\n";
		result += "Date : " + this.getDateDebut() + "\n";
		result += "quantite : " + this.getQuantity() + "\n";
		result += "montant : " + this.getMontant() + "\n";

		return result;
	}
}
