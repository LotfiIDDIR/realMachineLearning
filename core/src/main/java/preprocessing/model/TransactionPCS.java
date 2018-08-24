package fr.ensma.lias.bimedia2018machinelearning.preprocessing.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.generation.utilities.GenerDate;
import fr.ensma.lias.bimedia2018machinelearning.generation.utilities.GenerNum;
import fr.ensma.lias.bimedia2018machinelearning.learning.Iclassification;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSV;

/**
 * @author  Lotfi IDDIR
 */

public class TransactionPCS extends Transaction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7310659671907084169L;
	private int quantite;
	
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
	@Override
	public ICSV readCSV(String[] line,boolean treated) {// Treated means that the line is from a treated file
		
		TransactionPCS pcs = new TransactionPCS();
		if(treated)
		{
			pcs.setPDV(Integer.parseInt(line[0]));
			pcs.setDateDebut(new Timestamp(Long.parseLong(line[1])));
			pcs.setQuantite(Integer.parseInt(line[2]));
			pcs.setMontant(Double.parseDouble(line[3]));
			pcs.setFraud(Integer.parseInt(line[4]));
		}
		else// if not treated
		{
			try
			{
				pcs.setDateDebut(Timestamp.valueOf(line[6]));// We have to verify and transform the dateformat
			}
			catch(IllegalArgumentException e)
			{
				try {
					DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					DateFormat dateformat2 = new SimpleDateFormat("dd/MM/yyyy");
					Date date = new Date(dateformat2.parse(line[6]).getTime());
					String dateString=dateformat.format(date).toString();
					pcs.setDateDebut(Timestamp.valueOf(dateString));
					
					} catch (ParseException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
			}
			pcs.setPDV(Integer.parseInt(line[2]));
			pcs.setQuantite(1);// In non treated file, one line is one PCS
			pcs.setMontant(Double.parseDouble(line[5]));
			pcs.setFraud(Integer.parseInt(line[7]));
		}
		
		return pcs;
	}
	@Override
	public String writeCSV(char separator) {//How to write one transaction object in one CSV line 
		
		String result="";
		result+=Integer.toString(pdv);
		result+=separator;
		//DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	//	result+=dateformat.format(new Date(dateDebut.getTime())).toString();// Change the date format to write it directly as a timestamp
		result+=Long.toString(dateDebut.getTime()%86400000);
		result+=separator;
		result+=Integer.toString(quantite);
		result+=separator;
		result+=Double.toString(montant);
		result+=separator;
		result+=Integer.toString(fraud);
		return result;
	}
	@Override
	public LabeledPoint convertToLabeledPoint() {
		
		double label = (double)this.getFraud();
		double[] features = {(double)(Math.round(this.getPdvCluster())),(double)(this.getDateDebut().getTime()%86400000),(double)(this.getQuantite()),(double)(this.getMontant())};
		this.setDateNoTime(this.getDateDebut().getTime()/86400000);
		Vector vector = new DenseVector(features);
		LabeledPoint labeledPoint = new LabeledPoint(label,vector);
		return labeledPoint;
	}
	
	@Override
	public Iclassification convertToIclassification(LabeledPoint point) {
		TransactionPCS t = new TransactionPCS();
		t.setFraud((int)point.label());
		t.setPDV((int)point.features().apply(0));
		t.setDateDebut(new Timestamp(Math.abs((long)(point.features().apply(1)))));
		t.setQuantite((int)point.features().apply(2));
		t.setMontant(point.features().apply(3));
		return t;
	}

	public String toString()
	{
		String result="";
		result+= "PDV : "+this.getPDV()+"\n";
		result+= "Date : "+this.getDateDebut()+"\n";
		result+= "quantite : "+this.getQuantite()+"\n";
		result+= "montant : "+this.getMontant()+"\n";
		
		return result;
	}
	
	@Override
	public TransactionPCS randomInit(List<String> pdvVictimes) {
		GenerDate dateGen = new GenerDate();
		GenerNum numGen = new GenerNum();
		this.setDateDebut(dateGen.generTimestamp("2017-05-24 08:00:00","2017-05-27 18:00:00"));
		this.setFraud(1);
		this.setQuantite(numGen.generFromInterval(1, 81));
		this.setMontant(this.getQuantite()*250.0);
		this.setPDV(numGen.generFromList(pdvVictimes));
		return this;
	}
	@Override
	public String getVictimsPath() {
		return "C:\\Users\\ASUS\\Desktop\\PCSResources\\PDVVictimesPCS.csv";
	}
	@Override
	public LabeledPoint convertToLabeledPoint(String line,char separator) {
		String[] parts = line.split(""+separator);
        return new LabeledPoint(Double.parseDouble(parts[4]),
                Vectors.dense(Double.parseDouble(parts[0]),
                		 Double.parseDouble(parts[1]),
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3])));
	}
	@Override
	public String writeFraudDetails(Vector v) {
		String result = "";
		LocalDateTime date = (new Timestamp((long)v.apply(1))).toLocalDateTime();
		result+="Fraud detectée à "+date.getHour()+"h "+date.getMinute();
		result+=" de "+v.apply(2)+" PCS et d'un montant total de "+v.apply(3)+" Chez le PDV : "+v.apply(0);
		return result;
	}
	public int getFraudIndex()
	{
		return 4;
	}
	@Override
	public Vector toVector(String []line)// Not generelized
	{
	    double[] features = {Double.parseDouble(line[0]),Double.parseDouble(line[1]),Double.parseDouble(line[2]),Double.parseDouble(line[3])};
	    Vector vector = new DenseVector(features);
	    return vector;
	}
	@Override
	public Vector toVector() {
	    double[] features = {pdv,dateDebut.getTime()%86400000,quantite,montant};
	    Vector vector = new DenseVector(features);
	    return vector;
	}
	@Override
	public String writeLabeledPoint(LabeledPoint point,char separator) {
	    String out="";
	    for(int i =0; i<point.features().size();i++)
	    {
		if(i==1)
		    out+=point.features().apply(i);
		else
		    out+=Math.round(point.features().apply(i));
		out+=separator;
	    }
	    out+=point.label();
	    return out;
	}
}
