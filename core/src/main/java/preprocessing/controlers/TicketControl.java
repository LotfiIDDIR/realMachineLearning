package fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSVControl;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.OperationLine;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Produit;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Ticket;

/**
 * @author IDDIR Lotfi
 */
public class TicketControl implements ICSVControl {
	private List<Ticket> ticketList;
	private List<OperationLine> OperationList;
	
	public TicketControl() {
		super();
		this.setTicketList(new ArrayList<Ticket>());
		this.setOperationList(new ArrayList<OperationLine>());
	}

	//Getters and Setters
	public List<Ticket> getTicketList() {
		return ticketList;
	}

	public void setTicketList(List<Ticket> ticketList) {
		this.ticketList = ticketList;
	}

	public List<OperationLine> getOperationList() {
		return OperationList;
	}

	public void setOperationList(List<OperationLine> operationList) {
		OperationList = operationList;
	}

	
	public void setTickettList(String path,char separator) throws IOException {// One CSV File must contain ONLY ONE PDV operations 
		
		CSVUsage csvreader = new CSVUsage(path,separator);
		csvreader.read(this, true,false);//Fills the List of operations from the CSV Files
	}
	
	@Override
	public void addCSVObject(String[] line,boolean treated) {//Add a transaction to the list from a CSV line 
		if(treated)
		{
			Ticket ticket = new Ticket();
			ticket.readCSV(line, treated);
			this.getTicketList().add(ticket);
		}
		else
		{
			OperationLine  operline = new OperationLine();
			operline.readCSV(line, true);
			this.getOperationList().add(operline);
		}
		
	}

	@Override
	public List<String> writeCSVObject(char separator) {// Prepare the list to be written on a CSV File
		List<String>liste = new ArrayList<String>();
		for(Ticket ticket : this.getTicketList())
		{
			liste.add(ticket.writeCSV(separator));
		}
		return liste;
	}

	// Grouping similar operations concerning the same ticket
	public void fusion(String pathSource,String pathDest,char separator) throws IOException
	{
		CSVUsage csvreader = new CSVUsage(pathSource,separator);
		csvreader.read(this, false,true);//Fills the List of operations from the CSV Files
		List<Ticket>newList = new ArrayList<Ticket>();
		for(OperationLine operation:this.getOperationList())// For each transaction
		{
			Ticket lastTicket = containsTicket(newList,operation);
			if(lastTicket!=null)
			{
				mergeOperations(lastTicket,operation);
			}
			else
			{
				Ticket newTicket = new Ticket();
				newTicket.setTicket_id(operation.getTicket());
				newTicket.getProduits().add(new Produit(operation.getProduit()));
				newList.add(newTicket);
			}
		}
		this.setTicketList(newList);
		newList.removeIf((Ticket elem)-> elem.getProduits().size()<2);
		csvreader.write(pathDest, this);// At the end we write the List on a CSV File
	}
	
	// Function that returns the last transaction on the given list that has the same PDV property as the input transact
	public Ticket containsTicket(List<Ticket>liste, OperationLine operation)
	{
		Ticket found=null;
		if(liste!=null)
		{
			for(Ticket elem : liste)
			{
				if (elem.getTicket_id()==operation.getTicket())
				{
					found=elem;
				}
			}
		}
		return found;
	}
	
	// Function that merges the second transaction to the first with date property equals to the earliest
	public void mergeOperations(Ticket ticket, OperationLine operation)
	{
		ticket.getProduits().add(new Produit(operation.getProduit()));
	}	
}
