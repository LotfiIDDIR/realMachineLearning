package fr.ensma.lias.bimedia2018machinelearning.generation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionContext;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Transaction;

/**
 * @author  Lotfi IDDIR
 */

public class GenerTransaction {
	
	private List<Transaction>nonFrauds;
	private List<Transaction>frauds;
	public static List<String> pdvVictimes;
	
	//Getters and Setters
	public List<Transaction> getNonFrauds() {
		return nonFrauds;
	}

	public void setNonFrauds(List<Transaction> nonFrauds) {
		this.nonFrauds = nonFrauds;
	}

	public List<Transaction> getFrauds() {
		return frauds;
	}

	public void setFrauds(List<Transaction> frauds) {
		this.frauds = frauds;
	}
	
	//Constructor
	public GenerTransaction() {
		super();
		this.frauds=new ArrayList<Transaction>();
		this.nonFrauds=new ArrayList<Transaction>();	
	}
	
	public static void fillVictims(String path, char sep)
	{
		CSVUsage csv = new CSVUsage(path,sep);
		try {
			pdvVictimes = csv.readColumn(0, false);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public void increase(String path,char separator,int k,TransactionContext context, int indexCol) throws IOException// increases the number of transactions of the given file k times
	{
		FileWriter fw ;
		BufferedWriter bw ;
		double ratio = this.extractNonFrauds(path, separator,context, indexCol);
		int num = (int)((double)nonFrauds.size()/ratio);
		int fraudSize=(int) (num*(1.0-ratio));

		for (int i =0;i<k;i++)
		{
			fw = new FileWriter(path,true);
			bw = new BufferedWriter(fw,250000000);
			List<Transaction>generated =this.generTransactions(nonFrauds.subList(i*nonFrauds.size()/k,(i+1)*nonFrauds.size()/k), k, 1);
			for (Transaction t : generated)
			{
				context.setTransact(t);
				bw.write(context.writeCSV(separator));
				bw.newLine();
			}
			bw.close();			
		}
		fw = new FileWriter(path,true);
		bw = new BufferedWriter(fw,250000000);
		for(int i =0;i<k*fraudSize;i++)
		{
			context.initTransaction();
			//Transaction t = context.initTransaction();
			//context.setTransact(t);
			bw.write(context.writeCSV(separator));
			bw.newLine();
		}
		bw.close();
	}
	
	public double extractNonFrauds (String path,char separator,TransactionContext context, int indexCol) throws IOException// Function that extracts frauds from the given file
	{
		CSVUsage csv = new CSVUsage(path,separator);
		String[] nextLine = null;
		int total=0;
		List<Transaction>output= new ArrayList<Transaction>();
		while ((nextLine = csv.getCsvreader().readNext()) != null) 
		{
			total++;
			if(nextLine[indexCol].equals("0"))
			{
				Transaction t = (Transaction) context.readCSV(nextLine, true);
				output.add(t);
			}
		}
		this.setNonFrauds(output);
		double ratio = (double)output.size()/(double)total;
		return ratio;
	}
	
	public List<Transaction>generTransactions(List<Transaction>inputList,int k,int num)//Generate using SMOTE implementation
	{
		List<Transaction>output = new ArrayList<Transaction>();
		List<LabeledPoint>points = new ArrayList<LabeledPoint>();
		List<LabeledPoint>syntheticPoints = new ArrayList<LabeledPoint>();
		for(Transaction elem : inputList)
		{
			points.add(elem.convertToLabeledPoint());
		}
		GenerLabeledPoints generator = new GenerLabeledPoints();
		for(LabeledPoint point : points)
		{
			syntheticPoints.addAll(generator.generRandomNeighboors(points, point, k,num));
		}
		points=null;
		int index=0;
		for(Transaction t: inputList)
		{
			for(int i=0;i<k*num;i++)
			{
				Transaction t2= (Transaction) t.convertToIclassification(syntheticPoints.get(index+i));
				t2.setDateNoTime(t.getDateNoTime());
				t2.setDateDebut(new Timestamp((long) (t2.getDateNoTime()*86400000+syntheticPoints.get(index+i).features().apply(1))));
				output.add(t2);
			}
			index=index+k;
		}
		syntheticPoints=null;
		return output;
		
	}
		
	public List<Transaction>duplicateTransactions(List<Transaction>inputList,int k)//Duplicates the inputList k times
	{
		List<Transaction>output= new ArrayList<Transaction>();
		for(int i=0;i<k;i++)
		{
			output.addAll(inputList);
		}
		return output;
		
	}

}
