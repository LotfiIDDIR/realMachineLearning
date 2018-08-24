package fr.ensma.lias.bimedia2018machinelearning;

import fr.ensma.lias.bimedia2018machinelearning.learning.Classifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.RandomForestClassifier;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;
import fr.ensma.lias.bimedia2018machinelearning.prediction.Predictor;

import java.io.IOException;

import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;

public class PredictionMain {
    public static void main( String[] args ) 
    {
    
	Classifier dtc = new RandomForestClassifier();// Gotta generelize this with a a parameter
	PredictionContext ctx = new PredictionContext();
	ctx.setClassifier(dtc);
	Predictor predictor = new Predictor("predictionApp","local[*]");
	dtc.setJsc(predictor.getJsc());
	dtc.setModel("C:\\eclipse-workspace\\bimedia2018machinelearning\\bimedia2018machinelearning-backend\\bimedia2018machinelearning-server\\src\\main\\resources\\models\\myRandomForestClassificationModel1528106154111");
	double [] features = {0,26704434,32,8000.0};
	Vector v= new DenseVector(features);
	try {
	    System.out.println(predictor.predict(v, ctx));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
