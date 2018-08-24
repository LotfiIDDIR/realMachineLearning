package fr.ensma.lias.bimedia2018machinelearning.prediction;

import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;

/**
 * @author Lotfi IDDIR
 */
public class Predictor {

	private String appName;

	private String master;

	static JavaSparkContext jsc;

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

	public JavaSparkContext getJsc() {
		return jsc;
	}

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

	public double predict(Vector v, PredictionContext contextP) throws IOException {
		return contextP.predict(v);
	}

	public Vector toVector(String[] line) {
		double[] features = { Double.parseDouble(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]),
				Double.parseDouble(line[3]) };
		Vector vector = new DenseVector(features);
		return vector;
	}
}
