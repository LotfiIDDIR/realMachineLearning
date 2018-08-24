package fr.ensma.lias.bimedia2018machinelearning.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class BufferDTO {

	private int pdv;

	private double total;

	private String dateDebut;

	private int quantite;

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

	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getDateDebut() {
		return dateDebut;
	}

	@JsonProperty("commerce")
	public int getPdv() {
		return pdv;
	}

	public void setPdv(int pdv) {
		this.pdv = pdv;
	}

	@JsonProperty("total")
	public double getMontant() {
		return total;
	}

	public void setMontant(double total) {
		this.total = total;
	}

	public BufferDTO() {
	}
}
