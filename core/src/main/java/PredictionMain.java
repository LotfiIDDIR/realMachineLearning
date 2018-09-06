package fr.ensma.lias.bimedia2018machinelearning;

import fr.ensma.lias.bimedia2018machinelearning.learning.Classifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.RandomForestClassifier;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;
import fr.ensma.lias.bimedia2018machinelearning.prediction.Predictor;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;

public class PredictionMain {
    public static void main( String[] args ) 
    {
    
	/*Classifier dtc = new RandomForestClassifier();// Gotta generelize this with a a parameter
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
	}*/
    	Pattern patern = Pattern.compile("[1-9]?.[0-9]+E[1-9]?");
    	String test = " this is a numbre feature5>1.55555E72.0232E6";
    	Matcher matcher = patern.matcher(test);
    	String result = test;
    	 while(matcher.find()) {
    		 String sub = test.substring(matcher.start(), matcher.end());
    		 String date = (new Timestamp((long) Double.parseDouble(sub))).toString();
    		 String time = date.substring(date.length()-10, date.length());
    		 result = result.replace(sub, time);
    		 System.out.println(result);
         }
    }

}
