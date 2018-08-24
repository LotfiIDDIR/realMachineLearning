package fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSVControl;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Transaction;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.TransactionPCS;

/**
 * @author  Lotfi IDDIR
 */
public class TransactionControl implements ICSVControl {
	private List<Transaction> transactList;
	private TransactionContext context;
	
	
	public TransactionControl() {
		super();
		this.transactList=new ArrayList<Transaction>();
	}

	//Getters and Setters
	
	public List<Transaction> getTransactList() {
		return transactList;
	}

	public void setTransactList(List<Transaction> transactList) {
		this.transactList = transactList;
	}
	public TransactionContext getContext() {
		return context;
	}

	public void setContext(TransactionContext context) {
		this.context = context;
	}

	public void setTransactList(String path,char separator,boolean withHeader) throws IOException {// Gotta change this
		
		CSVUsage csvreader = new CSVUsage(path,separator);
		csvreader.read(this, true,withHeader);//Fills the List of transactions from the CSV Files
	}
	
	@Override
	public void addCSVObject(String[] line,boolean treated) {//Add a transaction to the list from a CSV line 
		Transaction transact = (Transaction) context.readCSV(line, treated);
		
		transactList.add(transact);
		
	}

	@Override
	public List<String> writeCSVObject(char separator) {// Prepare the list to be written on a CSV File
		List<String>liste = new ArrayList<String>();
		for(Transaction elem: transactList)
		{
			liste.add(elem.writeCSV(separator));
		}
		return liste;
	}

	// Grouping similar transactions in one adding quantity field from a non treated CSV File
	public int fusion(long interval,String pathDest,char separator) throws IOException// interval is the interval time in millisec between each transaction
	{
		CSVUsage csv = new CSVUsage();
		csv.setSeparator(separator);
		List<Transaction>newList = new ArrayList<Transaction>();
		for(Transaction transact:this.getTransactList())// For each transaction
		{
			
			TransactionPCS lastTransact = (TransactionPCS) containsPDV(newList,transact);
			if(lastTransact!=null)
			{
				if(mergeable(lastTransact,(TransactionPCS) transact,interval))// We verify if the actual transaction is mergeable with one on the list
				{
					mergeTransactions(lastTransact,(TransactionPCS) transact);
				}
				else
				{
					newList.add((TransactionPCS) transact);
				}
			}
			else
			{
				newList.add((TransactionPCS) transact);
			}
		}
		this.setTransactList(newList);
		csv.write(pathDest, this);// At the end we write the List on a CSV File
		return newList.size();
	}
	
	// Function that returns the last transaction on the given list that has the same PDV property as the input transact
	public Transaction containsPDV(List<Transaction>liste, Transaction transact)
	{
		Transaction found=null;
		if(liste!=null)
		{
			for(Transaction elem : liste)
			{
				if (elem.getPDV()==transact.getPDV())
				{
					
					found=elem;
				}
			}
		}
		return found;
	}
	
	// Function that tells if two given transactions has the same PDV_id and time difference lower than given interavl
	public boolean mergeable(TransactionPCS transact1, TransactionPCS transact2,long interval)
	{
		boolean merge=false;
		if(transact1!=null && transact2!=null)
		{
			if (transact1.getPDV()==transact2.getPDV())
			{
				long diff=Math.abs(transact1.getDateDebut().getTime()-transact2.getDateDebut().getTime());
				if(diff<interval)
				{
					merge=true;
				}
			}
		}
		return merge;
	}
	
	// Function that merges the second transaction to the first with date property equals to the earliest
	public void mergeTransactions(TransactionPCS transact1, TransactionPCS transact2)
	{
		if(transact1.getFraud()+transact2.getFraud()>0)
		{
			transact1.setFraud(1);
		}
		transact1.setMontant(transact1.getMontant()+transact2.getMontant());
		transact1.setQuantite(transact1.getQuantite()+transact2.getQuantite());
		if(transact1.getDateDebut().getTime()>transact2.getDateDebut().getTime())
		{
			transact1.setDateDebut(transact2.getDateDebut());
		}
	}
	
	public void fillClusters(String mapFile,char separator) throws IOException
	{
	    CSVUsage csv = new CSVUsage(mapFile,separator);
	    String [] line = null;
	    Map<Integer,Integer> clusterMap = new HashMap<Integer,Integer>();
	    
	    while((line = csv.getCsvreader().readNext())!= null)
            {
        	clusterMap.put(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
            }
	    for (Transaction elem : this.getTransactList())
	    {
		if(clusterMap.containsKey(elem.getPDV()))
		{
		    elem.setPdvCluster(clusterMap.get(elem.getPDV()));
		}
		else
		{
		    elem.setPdvCluster(2);
		}
	    }
	}
	
	public void persistLabeledPoints(String path,char separator) throws IOException
	{
		FileWriter fw = new FileWriter(path,true);
		BufferedWriter bw = new BufferedWriter(fw,250000000);
		for (Transaction elem : this.getTransactList())
		{
			LabeledPoint point = elem.convertToLabeledPoint();
			for (int i = 0; i<point.features().size();i++)
			{
			    bw.write(Double.toString((double)Math.round(point.features().apply(i))));bw.write(separator);
			}
			bw.write(Double.toString((double)Math.round(point.label())));
			bw.newLine();
		}
		bw.close();
	}
}
