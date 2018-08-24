package fr.ensma.lias.bimedia2018machinelearning.prediction;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;

/**
 * @author Lotfi IDDIR
 */
public abstract class Classifier {

	protected String appName;

	protected String master;

	protected String modelTodebug;

	
	static JavaSparkContext jsc;

	public abstract double predict(Vector v);
	
	public abstract void setModel(String path);

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

	public void initClassifier(String appName, String master) {
		this.appName = appName;
		this.master = master;
		SparkConf sparkConf = new SparkConf().setAppName(appName).setMaster(master);
		sparkConf.set("spark.driver.cores", "2");
		sparkConf.set("spark.driver.memory", "2g");
		sparkConf.set("spark.executor.memory", "2g");
		sparkConf.set("spark.executor.heartbeatInterval", "30s");
		jsc = new JavaSparkContext(sparkConf);
	}
}
