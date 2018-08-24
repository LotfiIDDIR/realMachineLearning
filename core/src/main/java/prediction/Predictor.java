package fr.ensma.lias.bimedia2018machinelearning.prediction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionContext;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionControl;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;

/**
 * @author  Lotfi IDDIR
 */

public class Predictor {
	
	private String appName;
	private String master;
	static JavaSparkContext jsc;
	
	//Getters and Setters
	 public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public JavaSparkContext getJsc()
	{
	    return jsc;
	}
	//Constructor
	public Predictor(String appName, String master) {
		super();
		this.appName = appName;
		this.master = master;
		SparkConf sparkConf = new SparkConf().setAppName(appName).setMaster(master);
		sparkConf.set("spark.driver.cores", "2");
		sparkConf.set("spark.driver.memory", "2g");
		sparkConf.set("spark.executor.memory", "2g");
		sparkConf.set("spark.executor.heartbeatInterval", "30s");
		
	    jsc = new JavaSparkContext(sparkConf);
	}
	
	public void predict (String pathModel,String pathToPredict,char separator,TransactionContext context,PredictionContext contextP) throws IOException
	{
	    String temp = this.transform(pathToPredict, separator);
	    contextP.setModel(pathModel);
	    FileWriter fw = new FileWriter("tmp/output"+System.currentTimeMillis()+".txt",true);
	    BufferedWriter bw = new BufferedWriter(fw,250000000);
	    CSVUsage csv = new CSVUsage(temp,separator);
	    String [] nextLine=null;
	    while ((nextLine = csv.getCsvreader().readNext()) != null) 
	    {
		Vector v = this.toVector(nextLine);
		if(contextP.predict(v)>0.0)
		{
		    bw.write(context.writeFraudDetail(v));
		    bw.newLine();
		}
	    }
	    bw.close();	
	}
	
	public double predict (Vector v,PredictionContext contextP) throws IOException
	{
	    return contextP.predict(v);	
	}
	
	public String transform(String path, char separator) throws IOException// Gotta change this
	{
	    TransactionControl controller=new TransactionControl();
	    String output = "tmp/transformedPredictions"+System.currentTimeMillis()+".csv";
	    controller.fusion(10*60*1000,output,separator); 
	    return output;
	}
	
	public Vector toVector(String []line)// Not generelized
	{
	    double[] features = {Double.parseDouble(line[0]),Double.parseDouble(line[1]),Double.parseDouble(line[2]),Double.parseDouble(line[3])};
	    Vector vector = new DenseVector(features);
	    return vector;
	}

}
