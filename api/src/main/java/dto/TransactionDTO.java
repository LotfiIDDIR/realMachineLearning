package fr.ensma.lias.bimedia2018machinelearning.dto;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class TransactionDTO {

	private double montant;

	private int pdv;// le clicod du point de vente

	private String dateDebut;

	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(String dateDebut) {
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
}
