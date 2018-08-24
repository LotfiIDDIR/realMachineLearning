package fr.ensma.lias.bimedia2018machinelearning.preprocessing.model;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSV;

/**
 * @author IDDIR Lotfi
 *
 */
public class OperationLine implements ICSV{
	
	private String produit;
	private int ticket;
	private int quantite;
	
	//Getters and Setters
	public String getProduit() {
		return produit;
	}
	public void setProduit(String produit) {
		this.produit = produit;
	}
	public int getTicket() {
		return ticket;
	}
	public void setTicket(int ticket) {
		this.ticket = ticket;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
	@Override
	public ICSV readCSV(String[] line, boolean treated) {
		if(treated)
		{
			String peer = line[0].substring(1, line[0].length()-1);
			line = peer.split(",");
			this.setProduit(line[1]);
			this.setTicket(Integer.parseInt(line[0]));
			this.setQuantite(1);
		}
		else
		{
			this.setProduit(line[0]);
			this.setTicket(Integer.parseInt(line[4]));
			this.setQuantite(Integer.parseInt(line[2]));
		}
		return this;
	}
	@Override
	public String writeCSV(char separator) {
		String result = "";
		result+=this.getProduit();
		result+=separator;
		result+=Integer.toString(this.getTicket());
		return result;
	}
}
