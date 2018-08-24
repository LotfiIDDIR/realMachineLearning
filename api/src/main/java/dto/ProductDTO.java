package fr.ensma.lias.bimedia2018machinelearning.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class ProductDTO {

	private String code;

	private String designation;

	private double amount;

	private int stockQuantity;

	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

	public double getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDesignation() {
		return designation;
	}

	@JsonProperty("designation")
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	@JsonProperty("stockQuantity")
	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
}
