package fr.ensma.lias.bimedia2018machinelearning.preprocessing.model;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.learning.Iclassification;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSV;

/**
 * @author  Lotfi IDDIR
 */

public class TransactionMoneyGram extends Transaction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5009440081832338958L;
	private int id;
	private String country;
	private String purpose;
	private String paymentType;
	private String originFund;
	private String currency;
	private double changeRate;
	static Map<String,Integer> paymentCategories = new TreeMap<String,Integer>();
	static Map<String,Integer> purposeCategories = new TreeMap<String,Integer>();
	static Map<String,Integer> originCategories = new TreeMap<String,Integer>();
	static Map<String,Double> currencies = new TreeMap<String,Double>();
	
	
	//Getters and Setters
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getOriginFund() {
		return originFund;
	}
	public void setOriginFund(String originFund) {
		this.originFund = originFund;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getChangeRate() {
		return changeRate;
	}
	public void setChangeRate(double changeRate) {
		this.changeRate = changeRate;
	}

	//Constructurs
	public TransactionMoneyGram() {
		
	}
	
	public static void initCategories() {
		paymentCategories.put("Espèce", 0);
		paymentCategories.put("Chèque", 1);
		paymentCategories.put("Carte bancaire", 2);
		purposeCategories.put("Obligations légales", 0);
		purposeCategories.put("Salaire", 1);
		purposeCategories.put("Remboursements professionnels", 2);
		purposeCategories.put("Etudes et formation", 3);
		purposeCategories.put("Investissement / épargne", 4);
		purposeCategories.put("Autres", 5);
		purposeCategories.put("Achat entre particuliers", 6);
		purposeCategories.put("Prêt", 7);
		purposeCategories.put("Donation et aides financières", 8);
		purposeCategories.put("Soutien familial", 9);
		purposeCategories.put("Cadeau", 10);
		originCategories.put("Heritage", 0);
		originCategories.put("Cadeau", 1);
		originCategories.put("Cadeaux", 1);
		originCategories.put("Allocations", 2);
		originCategories.put("Autres", 3);
		originCategories.put("Salaire", 4);
		originCategories.put("Revenus (salaire; retraite)", 4);
		originCategories.put("Economies", 5);
		originCategories.put("Epargne", 6);
		originCategories.put("Loyer", 7);
		originCategories.put("Vente", 8);
		CSVUsage csv = new CSVUsage("src/main/resources/references/currencies.csv",';');
		String [] line = null;
		try {
			while((line = csv.getCsvreader().readNext())!= null)	
				currencies.put(line[1], Double.parseDouble(line[0]));
			} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
	}
	
	@Override
	public ICSV readCSV(String[] line,boolean treated) {// Treated means that the line is from a treated file
		TransactionMoneyGram mg = new TransactionMoneyGram();
		if(treated)// To change
		{
			mg.setPdvCluster((int)Double.parseDouble(line[2]));
			mg.setDateDebut(new Timestamp((new Double(Double.parseDouble(line[1]))).longValue()));
			mg.setMontant(Double.parseDouble(line[0]));
			mg.setPurpose(this.getLastKeyByValue(purposeCategories,(int)Double.parseDouble(line[3])));
			mg.setPaymentType(this.getLastKeyByValue(paymentCategories,(int)Double.parseDouble(line[4])));
			mg.setOriginFund(this.getLastKeyByValue(originCategories,(int)Double.parseDouble(line[5])));
			mg.setFraud((int)Double.parseDouble(line[7]));
			mg.setChangeRate(Double.parseDouble(line[6]));
		}
		else// if not treated
		{
			try
			{
			    mg.setDateDebut(Timestamp.valueOf(line[3]));// We have to verify and transform the dateformat
			}
			catch(IllegalArgumentException e)
			{
				try {
					DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					DateFormat dateformat2 = new SimpleDateFormat("dd/MM/yyyy");
					Date date = new Date(dateformat2.parse(line[3]).getTime());
					String dateString=dateformat.format(date).toString();
					mg.setDateDebut(Timestamp.valueOf(dateString));
					
					} catch (ParseException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
			}
			mg.setId(Integer.parseInt(line[0]));
			mg.setMontant(Double.parseDouble(line[1]));
			mg.setCurrency(line[2]);
			mg.setPDV(Integer.parseInt(line[4]));
			mg.setPurpose(line[5]);
			mg.setPaymentType(line[6]);
			mg.setOriginFund(line[7]);
			mg.setFraud(Integer.parseInt(line[8]));
			if (mg.getFraud()==1)
			{
			    mg.setFraud(0);
			}
			else
			{
			    mg.setFraud(1);
			}
			
		}
		
		return mg;
	}
	@Override
	public String writeCSV(char separator) {//How to write one transaction object in one CSV line 
		
		String result="";
		result+=Integer.toString(id);
		result+=separator;
		result+=Double.toString(montant);
		result+=separator;
		result+=country;
		result+=separator;
		result+=Long.toString(dateDebut.getTime()%86400000);
		result+=separator;
		result+=Integer.toString(pdv);
		result+=separator;
		result+=purpose;
		result+=separator;
		result+=paymentType;
		result+=separator;
		result+=originFund;
		result+=separator;
		result+=fraud;
		return result;
	}
	@Override
	public LabeledPoint convertToLabeledPoint() {// Add another feature here like country
		
		TransactionMoneyGram.initCategories();
		double label = this.getFraud();
		double[] features = {(double)(this.getMontant()),(double)(Math.round(this.getDateDebut().getTime()%86400000)),(double)(Math.round(this.getPdvCluster())),(double)(Math.round(this.categorizePurpose(this.getPurpose()))),
				(double)(Math.round(this.categorizePaymentType(this.getPaymentType()))),(double)(Math.round(this.categorizeOrgin(this.getOriginFund()))),currencies.get(this.getCurrency())};
		this.setDateNoTime(this.getDateDebut().getTime()/86400000);
		Vector vector = new DenseVector(features);
		LabeledPoint labeledPoint = new LabeledPoint(label,vector);
		return labeledPoint;
	}
	
	@Override
	public Iclassification convertToIclassification(LabeledPoint point) {
		TransactionMoneyGram t = new TransactionMoneyGram();
		t.setFraud((int)point.label());
		t.setPDV((int)point.features().apply(0));
		t.setDateDebut(new Timestamp(Math.abs((long)(point.features().apply(1)))));
		
		t.setMontant(point.features().apply(3));
		return t;
	}
	
	public int categorizeOrgin(String origin)
	{
		int output=-1;
		if(originCategories.containsKey(origin))
		{
			output=originCategories.get(origin);
		}
		else
		    System.out.println(origin);
		return output;
	}
	public int categorizePaymentType(String paymentType)
	{
		int output=-1;
		if(paymentCategories.containsKey(paymentType))
		{
			output=paymentCategories.get(paymentType);
		}
		else
		    System.out.println(paymentType);
		return output;
	}
	public int categorizePurpose(String purpose)
	{
		int output=-1;
		if(purposeCategories.containsKey(purpose))
		{
			output=purposeCategories.get(purpose);
		}
		else
		    System.out.println(purpose);
		return output;
	}
	
	public String getLastKeyByValue(Map<String,Integer> map, int value)
	{
		String output = "";
		for(String key : map.keySet())
		{
			if(map.get(key).equals(value))
			{
				output = key;
			}
		}
		return output;
	}
	
	@Override
	public TransactionMoneyGram randomInit(List<String> pdvVictimes) {
		return null;
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getVictimsPath() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public LabeledPoint convertToLabeledPoint(String line,char separator) {
		 String[] parts = line.split(""+separator);
         return new LabeledPoint(Double.parseDouble(parts[7]),
                 Vectors.dense(Double.parseDouble(parts[0]),
                 		 Double.parseDouble(parts[1]),
                         Double.parseDouble(parts[2]),
                         Double.parseDouble(parts[3]),
                         Double.parseDouble(parts[4]),
                         Double.parseDouble(parts[5]),
                         Double.parseDouble(parts[6])));
	}
	@Override
	public String writeFraudDetails(Vector v) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getFraudIndex()
	{
		return 6;
	}
	@Override
	public String toString() {
	    String result="";
	    result+= "PDV : "+this.getPDV()+"\n";
	    result+= "Date : "+this.getDateDebut()+"\n";
	    result+= "montant : "+this.getMontant()+"\n";	
	    return result;
	}
	@Override
	public Vector toVector() {
	    // TODO Auto-generated method stub
	    return null;
	}
	@Override
	public Vector toVector(String[] line) {
	    // TODO Auto-generated method stub
	    return null;
	}
	@Override
	public String writeLabeledPoint(LabeledPoint point, char separator) {
	    String out="";
	    for(int i =0; i<point.features().size();i++)
	    {
		if(i<2)
		    out+=point.features().apply(i);
		else
		    out+=Math.round(point.features().apply(i));
		out+=separator;
	    }
	    out+=point.label();
	    return out;
	}
}