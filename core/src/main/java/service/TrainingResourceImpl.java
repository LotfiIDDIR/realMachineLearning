package fr.ensma.lias.bimedia2018machinelearning.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import fr.ensma.lias.bimedia2018machinelearning.api.TrainingResource;
import fr.ensma.lias.bimedia2018machinelearning.balancing.FraudBalancer;
import fr.ensma.lias.bimedia2018machinelearning.dto.MetricsDTO;
import fr.ensma.lias.bimedia2018machinelearning.learning.Classifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.DecisionTreeClassifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.RandomForestClassifier;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionContext;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionControl;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Transaction;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.TransactionMoneyGram;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.TransactionPCS;

/**
 * @author Lotfi IDDIR 
 */
public class TrainingResourceImpl implements TrainingResource {

	@Override
	public MetricsDTO getMetrics(String product, String model, double fraudPortion, double trainingPortion, int nb,
			int interval, int numTree) {
		System.out.println("Je suis 1");
		int min= interval;
    	double finalRatio = fraudPortion;// Le ratio de fraudes voulu
    	double trainingSetPortion = trainingPortion;
    	int maxDepth=nb;
    	int numTrees=numTree;
    	String transactionType=product;
    	String predictionModel=model;
    	Transaction t;
    	String folder="C:/Users/ASUS/Desktop/PFE/Demo/";
    	MetricsDTO metrics = new MetricsDTO();
    	// Etape de Prétraitement
    	
    	TransactionControl controller=new TransactionControl();
    	Map<Integer,Integer> map =new HashMap<Integer,Integer>();
    	try {
    	    if (transactionType.equals("PCS"))
    	    {
    		t = new TransactionPCS();
    		map.put(0, 3);
    	    }
    	    else
    	    {
    		t = new TransactionMoneyGram();
    		map.put(2, 3);
    		map.put(3, 11);
    		map.put(4, 3);
    		map.put(5, 10);
    	    }
    	    String pathSource=folder+"Transaction"+transactionType+"/Bimedia"+transactionType+".csv";
    	    TransactionContext context = new TransactionContext(t);
    	    controller.setContext(context);
    	    System.out.println("Je suis 2");
    	  	CSVUsage csv = new CSVUsage(pathSource,';');
    	  	System.out.println("Je suis 3");
    	    csv.read(controller, false,true);//Fills the List of transactions from the CSV Files
    	    System.out.println("Je suis 4");
   	    if (transactionType.equals("PCS"))
	    {
    		controller.fusion(min*60*1000,folder+"Transaction"+transactionType+"/merged.csv",';');
    		//Fusionne les transactions similaires et proches de moins de min minutes
	    }
   	 System.out.println("Je suis 5");
   	    	controller.fillClusters(folder+"References/storesClustered.csv", ';');
   	    	System.out.println("Je suis 6");
    	    controller.persistLabeledPoints(folder+"Transaction"+transactionType+"/LabeledPoints"+transactionType+".csv", ';');
  
    	    System.out.println("Je suis 7");
    	//  Etape d'équilibrage		
    	   FraudBalancer frb= new FraudBalancer();
    	   frb.balance(folder+"Transaction"+transactionType+"/LabeledPoints"+transactionType+".csv",';', finalRatio,context);
    	   System.out.println("Je suis 8");
        // Etape d'apprentissage
    	    Classifier classifier;
    	    @SuppressWarnings("unused")
			String modelPath;
    	if (predictionModel.equals("DT"))
	    {
	    	classifier = new DecisionTreeClassifier("frauDetectionApp","local[*]");
	    	PredictionContext pContext = new PredictionContext();
	    	pContext.setClassifier(classifier);
    		modelPath = ((DecisionTreeClassifier)classifier).train(folder+"Transaction"+transactionType+"/LabeledPoints"+transactionType+".csv",';',trainingSetPortion,maxDepth,map,context);
    		classifier.getModelTodebug();
    		metrics.setError(classifier.getTestError());
        	metrics.setPrecision(classifier.getPrecision());
        	metrics.setRecall(classifier.getRappel());
	    }
	    else
	    {
			classifier = new RandomForestClassifier("frauDetectionApp","local[*]");
	    	PredictionContext pContext = new PredictionContext();
	    	pContext.setClassifier(classifier);
			modelPath = ((RandomForestClassifier)classifier).train(folder+"Transaction"+transactionType+"/LabeledPoints"+transactionType+".csv",';',trainingSetPortion,maxDepth,map,numTrees,context);
			classifier.getModelTodebug();
			metrics.setError(classifier.getTestError());
        	metrics.setPrecision(classifier.getPrecision());
        	metrics.setRecall(classifier.getRappel());
	    }
	    	classifier.getJsc().close();
    	} catch (IOException e) {
    	    // TODO Auto-generated catch block
    	    e.printStackTrace();
    	}
    	return metrics;
	}
	@Override
	public void upload(InputStream uploaded, FormDataContentDisposition fileDetail) {
		System.out.println(fileDetail.getFileName());
		String uploadedFileLocation = "C:/Users/ASUS/Downloads/" + fileDetail.getFileName();
	    saveToFile(uploaded,uploadedFileLocation);
	    System.out.println("saved");
	}
	private void saveToFile(InputStream uploadedInputStream,
	        String uploadedFileLocation) {
	    try {
	        OutputStream out = null;
	        int read = 0;
	        byte[] bytes = new byte[1024];
	        out = new FileOutputStream(new File(uploadedFileLocation));
	        while ((read = uploadedInputStream.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
	        out.flush();
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}