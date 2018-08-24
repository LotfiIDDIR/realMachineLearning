package fr.ensma.lias.bimedia2018machinelearning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import fr.ensma.lias.bimedia2018machinelearning.learning.Classifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.DecisionTreeClassifier;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;

public class PrintApp {

    public static void main( String[] args ) 
    {
	Classifier classifier = new DecisionTreeClassifier("frauDetectionApp","local[*]");
    	PredictionContext pContext = new PredictionContext();
    	pContext.setClassifier(classifier);
    	
    	classifier.setModel("C:/eclipse-workspace/bimedia2018machinelearning/bimedia2018machinelearning-backend/bimedia2018machinelearning-server/src/main/resources/myDecisionTreeClassificationModel1521130719844");
    	
    	
    	String a=(( DecisionTreeClassifier)classifier).getModel().toDebugString();
    	//System.out.println(a);
	
    	a=a.replace("feature 0", "cluster");
    	a=a.replace("feature 1", "time");
    	a=a.replace("feature 2", "quantity");
    	a=a.replace("feature 3", "amount");
    	a=a.replace(")",")"+System.getProperty("line.separator") );
    	a=a.replace("Else",System.getProperty("line.separator")+"Else");
    	String [] times = a.split("E7");
    	String [] timesToWrite= new String[times.length-1];
    	
    	for(int i=0;i<times.length-1;i++)
    	{
    	    char point=' ';
    	    int j =times[i].length()-1;
    	    while (point!='.')
    	    {
    		point=times[i].charAt(j);
    		j--;
    	    }
    	    times[i] = times[i].substring(j, times[i].length())+"E7";
    	    timesToWrite[i]=(new Timestamp((long) Double.parseDouble(times[i]))).toString();
    	    timesToWrite[i]= timesToWrite[i].substring( timesToWrite[i].length()-13, timesToWrite[i].length());
    	    a=a.replace(times[i],timesToWrite[i]);
    	}
    	
    	FileWriter fw;
	try {
	    fw = new FileWriter("tmp/forest5.txt",true);
	    BufferedWriter bw = new BufferedWriter(fw,250000000);
	    bw.write(a);
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }
}
