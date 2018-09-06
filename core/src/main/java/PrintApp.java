package fr.ensma.lias.bimedia2018machinelearning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ensma.lias.bimedia2018machinelearning.learning.Classifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.DecisionTreeClassifier;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;

public class PrintApp {

    public static void main( String[] args ) 
    {
	Classifier classifier = new DecisionTreeClassifier("frauDetectionApp","local[*]");
    	PredictionContext pContext = new PredictionContext();
    	pContext.setClassifier(classifier);
    	classifier.setModel("C:\\Users\\ASUS\\eclipse-workspace\\bimedia2018machinelearning\\bimedia2018machinelearning\\bimedia2018machinelearning-backend\\bimedia2018machinelearning-preprocessing\\tmp\\myDecisionTreeClassificationModelDepth3");
    	String a=(( DecisionTreeClassifier)classifier).getModel().toDebugString();
    	//System.out.println(a);
    	a=a.replace("feature 0", "cluster");
    	a=a.replace("feature 1", "time");
    	a=a.replace("feature 2", "quantity");
    	a=a.replace("feature 3", "amount");
    //	a=a.replace(")",")"+System.getProperty("line.separator") );
    	//a=a.replace("Else",System.getProperty("line.separator")+"Else");
    	Pattern patern = Pattern.compile("[1-9]?.[0-9]+E[1-9]?");
    	Matcher matcher = patern.matcher(a);
    	String result = a;
    	 while(matcher.find()) {
    		 String sub = a.substring(matcher.start(), matcher.end());
    		 String date = (new Timestamp((long) Double.parseDouble(sub))).toString();
    		 String[]parts = date .split(" ");
    		 result = result.replace(sub,parts[1]);
         }
    	
    	FileWriter fw;
	try {
	    fw = new FileWriter("tmp/test.txt",true);
	    BufferedWriter bw = new BufferedWriter(fw,250000000);
	    bw.write(result);
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }
}
