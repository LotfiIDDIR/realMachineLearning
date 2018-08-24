package fr.ensma.lias.bimedia2018machinelearning.preprocessing.model;


/**
 * @author IDDIR Lotfi
 *
 */
public class Produit {
	
	private String code_barre;
	private String designation;
	private double pu;
	
	//Getters and Setters
	public String getCode_barre() {
		return code_barre;
	}
	public void setCode_barre(String code_barre) {
		this.code_barre = code_barre;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public double getPu() {
		return pu;
	}
	public void setPu(double pu) {
		this.pu = pu;
	}
	
	
	//Constructors
	
	public Produit(String code_barre) {
		super();
		this.code_barre = code_barre;
	}
	public Produit() {
		super();
	}
	
	

}
