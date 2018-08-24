package fr.ensma.lias.bimedia2018machinelearning.prediction;

import org.apache.spark.mllib.linalg.Vector;

import fr.ensma.lias.bimedia2018machinelearning.learning.Classifier;

public class PredictionContext {
	
	private Classifier classifier;

	public Classifier getClassifier() {
	    return classifier;
	}

	public void setClassifier(Classifier classifier) {
	    this.classifier = classifier;
	}
	
	public double predict (Vector v)
	{
	    return classifier.predict(v);
	}
	public void setModel (String pathModel)
	{
	    classifier.setModel(pathModel);
	}

}
