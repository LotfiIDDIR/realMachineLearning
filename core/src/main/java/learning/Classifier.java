package fr.ensma.lias.bimedia2018machinelearning.learning;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionContext;

/**
 * @author  Lotfi IDDIR
 */

public abstract class Classifier {
	protected String appName;
	protected String master;
	protected String modelTodebug;
	protected double testError;
	protected double precision;
	protected double rappel;
	static JavaSparkContext jsc;
	//Getters and Setters
	public JavaSparkContext getJsc() {
	    return jsc;
	}
	public void setJsc(JavaSparkContext jsc2) {
	    jsc = jsc2;
	}
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
	public String getModelTodebug() {
	    return modelTodebug;
	}
	public void setModelTodebug(String modelTodebug) {
	    this.modelTodebug = modelTodebug;
	}
	public double getTestError() {
	    return testError;
	}
	public void setTestError(double testError) {
	    this.testError = testError;
	}
	public double getPrecision() {
	    return precision;
	}
	public void setPrecision(double prediction) {
	    this.precision = prediction;
	}
	public double getRappel() {
	    return rappel;
	}
	public void setRappel(double rappel) {
	    this.rappel = rappel;
	}
		
	public void initClassifier(String appName, String master)
	{
	    this.appName = appName;
	    this.master = master;
	    SparkConf sparkConf = new SparkConf().setAppName(appName).setMaster(master);
	    sparkConf.set("spark.driver.cores", "2");
	    sparkConf.set("spark.driver.memory", "2g");
	    sparkConf.set("spark.executor.memory", "2g");
	    sparkConf.set("spark.executor.heartbeatInterval", "30s");
	    jsc = new JavaSparkContext(sparkConf);
	}
		
	public JavaRDD<LabeledPoint> createRDD(String path,char separator,TransactionContext context)
	{    
	    JavaRDD<String> data = this.getJsc().textFile(path);
	    JavaRDD<LabeledPoint> parsedData = data
	            .map(new Function<String, LabeledPoint>() { /**
					 * 
					 */
				private static final long serialVersionUID = 1L;

				 public LabeledPoint call(String line) throws Exception {// Function that transforms a line to labeledPoint
	                   return context.convertToLabeledPoint(line, separator);
	                }
	            });
	    return parsedData;
	}
		
	//Function that converts a list of Iclassification objects to an RDD
	public JavaRDD<LabeledPoint> createRDD(List<Iclassification>liste)
	{
	    List<LabeledPoint>labeledPoints = new ArrayList<LabeledPoint>();
	    for(Iclassification elem:liste)
	    {
		labeledPoints.add(elem.convertToLabeledPoint());
	    }
	    JavaRDD<LabeledPoint> rdd = jsc.parallelize(labeledPoints,10000);
	    return rdd;
	}
		
	//Function that randomly splits dataset into trainingSet and testSet
	public JavaRDD<LabeledPoint>[] split(JavaRDD<LabeledPoint>dataset,double trainingSetPortion)
	{
	    return dataset.randomSplit(new double[] {trainingSetPortion,1.0-trainingSetPortion});
	}
		
	public abstract double predict (Vector v);
	public abstract void setModel(String path);

}
