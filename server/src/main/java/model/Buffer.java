package fr.ensma.lias.bimedia2018machinelearning.model;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;

import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class Buffer implements Comparable<Buffer> {

	private int pdv;
	
	private int pdvCluster;

	private double total;
	
	private Timestamp dateDebut;
	
	private int quantite;
	
	private Set<Transaction> transactions;

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
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

	public int getPdvCluster() {
	    return pdvCluster;
	}

	public void setPdvCluster(int pdvCluster) {
	    this.pdvCluster = pdvCluster;
	}
	@JsonProperty("total")
	public double getMontant() {
		return total;
	}

	public void setMontant() {
		double out = 0;
		for (Transaction t : transactions) {
			out += t.getMontant();
		}
		total = out;
	}

	public Timestamp getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut() {
		this.dateDebut = ((TreeSet<Transaction>) transactions).first().getDateDebut();
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void setQuantity() {
		quantite = this.transactions.size();
	}

	public int getQuantity() {
		return quantite;
	}

	public Buffer() {
		this.transactions = new TreeSet<Transaction>();
	}

	public String toString() {
		String result = "";
		result += "PDV : " + this.getPdv() + "\n";
		result += "Date : " + this.getDateDebut() + "\n";
		result += "quantite : " + this.quantite + "\n";
		result += "montant : " + this.getMontant() + "\n";

		return result;
	}

	public int compareTo(Buffer b) {
		try {
			return this.getDateDebut().compareTo(b.getDateDebut());
		} catch (NullPointerException e) {
			return 0;
		}
	}

	public void merge(Transaction transact2, long interval) {
		Set<Transaction> toRemove = new TreeSet<Transaction>();
		for (Transaction elem : this.transactions) {
			if ((Math.abs(elem.getDateDebut().getTime() - transact2.getDateDebut().getTime())) > interval) {
				toRemove.add(elem);
			}
		}
		this.transactions.removeAll(toRemove);
		this.transactions.add(transact2);
		this.setDateDebut();
		this.setMontant();
		this.setQuantity();
	}

	public Vector toVector() {
		if (quantite > 1) {
			double[] features = { pdvCluster, dateDebut.getTime() % 86400000, quantite, total };
			Vector vector = new DenseVector(features);
			return vector;
		} else {
			return ((TreeSet<Transaction>) transactions).first().toVector();
		}
	}
}
