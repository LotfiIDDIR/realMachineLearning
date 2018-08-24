package fr.ensma.lias.bimedia2018machinelearning.preprocessing.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSV;

/**
 * @author IDDIR Lotfi
 *
 */
public class Ticket implements ICSV{
	
	private int pdv;
	private int ticket_id;
	private Timestamp date;
	private List<Produit> produits;
	
	//Getters and Setters
	public int getPdv() {
		return pdv;
	}
	public void setPdv(int pdv) {
		this.pdv = pdv;
	}
	public int getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(int ticket_id) {
		this.ticket_id = ticket_id;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public List<Produit> getProduits() {
		return produits;
	}
	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}
	
	//Constructors
	
	public Ticket() {
		super();
		this.produits= new ArrayList<Produit>();
	}
	
	@Override
	public ICSV readCSV(String[] line, boolean treated) {
		
		if(treated)
		{
			for(int i =0; i<line.length;i++)
			{
				this.getProduits().add(new Produit(line[i]));
			}
		}
		return null;
	}
	
	@Override
	public String writeCSV(char separator) {
		String result = "";
		Set<String>newProducts = new TreeSet<String>();
		for (Produit produit : this.getProduits())
		{
			
			newProducts.add(produit.getCode_barre());
		}
		for (String elem : newProducts)
		{
			result+=elem;
			result+=separator;
		}
		return result.substring(0, result.length()-1);
	}
}
