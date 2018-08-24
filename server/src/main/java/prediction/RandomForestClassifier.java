package fr.ensma.lias.bimedia2018machinelearning.prediction;

import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import scala.Serializable;

public class RandomForestClassifier extends Classifier implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private RandomForestModel model;
	
	//Getters and Setters
	public RandomForestModel getModel() {
		return model;
	}

	public void setModel(RandomForestModel model) {
		this.model = model;
	}
	//Constructer
	public RandomForestClassifier(String appName, String master) {
		super.initClassifier(appName, master);
	}
	
	public RandomForestClassifier() {
	    
	}

	@Override
	public double predict(Vector v) {
		 return model.predict(v);	
	}

	@Override
	public void setModel(String path) {
		this.setModel(RandomForestModel
		          .load(jsc.sc(), path));
	}

}
