package fr.ensma.lias.bimedia2018machinelearning.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class Product implements Comparable<Product> {

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

	public boolean equals(Product product) {
		return (this.getCode().equals(product.getCode()));
	}

	@Override
	public int compareTo(Product product) {
		return (this.getCode().compareTo(((Product) product).getCode()));
	}
}
