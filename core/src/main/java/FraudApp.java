package fr.ensma.lias.bimedia2018machinelearning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.ensma.lias.bimedia2018machinelearning.balancing.FraudBalancer;
import fr.ensma.lias.bimedia2018machinelearning.learning.Classifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.DecisionTreeClassifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.RandomForestClassifier;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;
import fr.ensma.lias.bimedia2018machinelearning.prediction.Predictor;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionContext;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionControl;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Transaction;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.TransactionMoneyGram;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.TransactionPCS;


/**
 * @author Lotfi IDDIR 
 */
public class FraudApp 
{
    public static void main( String[] args ) 
    {
    	Options options = new Options();
    	options.addOption("intervalFusionInMinutes", true, "Specifie le temps separant deux transactions pour etre fusionnees");  
    	options.addOption("ratioFraud", true, "Specifie le ratio de fraude voulu entre 0.0 et 1.0 "); 
    	options.addOption("numDuplication", true, "Specifie le nombre de fois par lequel on veut multiplier la taille du fichier"); 
    	options.addOption("trainingSetPortion", true, "Specifie le ratio du dataset utilise pour l'entrainement valeur entre 0.0 et 1.0"); 
    	options.addOption("predictionModel", true, "Specifie le modèle d'apprentissage RF pour RandomForest, DT pour DecisionTree");
    	options.addOption("maxDepth", true, "Specifie la profondeur maximale de l'arbre de decision");
    	options.addOption("numTrees", true, "Specifie le nombre d'arbres à entrainer si le modèle de forêts aléatoires est choisi");
    	options.addOption("transactionType", true, "Specifie le type de transaction fraudée MG pour MoneyGram et PCS pour PCS");
    	CommandLineParser parser = new DefaultParser();
    	HelpFormatter formatter = new HelpFormatter();
    	int min=5;
    	double finalRatio = 0.5;// Le ratio de fraudes voulu
    	double trainingSetPortion = 0.5;
    	int maxDepth=3;
    	int numTrees=7;
    	String transactionType="MG";
    	String predictionModel="DT";
    	Transaction t;
    	String folder=System.getProperty("user.dir");
    	folder+="/src/main/resources/";
    	try
    	{
    	    CommandLine line = parser.parse(options, args);
    	    if(line.hasOption("intervalFusionInMinutes"))
    	    {
    		min=Integer.parseInt(line.getOptionValue("intervalFusionInMinutes"));
    	    }
    	    if(line.hasOption("ratioFraud"))
    	    {
    		finalRatio=Double.parseDouble(line.getOptionValue("ratioFraud"));
    	    }
    	    if(line.hasOption("trainingSetPortion"))
    	    {
    		trainingSetPortion=Double.parseDouble(line.getOptionValue("trainingSetPortion"));
    	    }
    	    if(line.hasOption("maxDepth"))
    	    {
    		maxDepth=Integer.parseInt(line.getOptionValue("maxDepth"));
    	    }
    	    if(line.hasOption("numTrees"))
    	    {
    	    	numTrees=Integer.parseInt(line.getOptionValue("numTrees"));
    	    }
    	    if(line.hasOption("transactionType"))
    	    {
    	    	transactionType=line.getOptionValue("transactionType");
    	    }
    	    if(line.hasOption("predictionModel"))
    	    {
    		predictionModel=line.getOptionValue("predictionModel");
    	    }
    	}catch(ParseException e)
    	{
    		formatter.printHelp("fraudApplication", options);
    	}
    	
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
    	  	CSVUsage csv = new CSVUsage(pathSource,';');
    	    csv.read(controller, false,true);//Fills the List of transactions from the CSV Files
   	    if (transactionType.equals("PCS"))
	    {
    		controller.fusion(min*60*1000,folder+"Transaction"+transactionType+"/merged10.csv",';');
    		//Fusionne les transactions similaires et proches de moins de min minutes
	    }
   	    	controller.fillClusters(folder+"References/storesClustered.csv", ';');
    	    controller.persistLabeledPoints(folder+"Transaction"+transactionType+"/LabeledPoints"+min+transactionType+".csv", ';');
  
    	
    	//  Etape d'équilibrage		
    	/*    FraudBalancer frb= new FraudBalancer();
    	    frb.balance(folder+"Transaction"+transactionType+"/LabeledPoints"+min+transactionType+".csv",';', finalRatio,context);
    	   
    	*/
    		// Etape de génération
        	
        	/*GenerTransaction gener = new GenerTransaction();
        	gener.increase("tmp/transformed"+debut+".csv",',', numDuplication);// ajouter indexCol et le contexte
        	*/
    		
        // Etape d'apprentissage
    	    Classifier classifier;
    	    String modelPath;
    	    String path = "C:\\Users\\ASUS\\Desktop\\PFE\\Rapport\\Statistiques\\"+transactionType+"\\DecisionTree"+min+".csv";
    	    FileWriter fw = new FileWriter(path,true);
    		BufferedWriter bw = new BufferedWriter(fw,250000000);
    		
    	if (predictionModel.equals("DT"))
	    {
    		bw.write("TrainingProportion;depth;error;precision;recall");
			bw.newLine();
    	    	classifier = new DecisionTreeClassifier("frauDetectionApp","local[*]");
    	    	PredictionContext pContext = new PredictionContext();
    	    	pContext.setClassifier(classifier);
    	    /*	for(int k = 3;k<10;k++)
    	    	{
    	    		maxDepth = k;
	    	    	for(int j=1;j<10;j++)
	    	    	{
	    	    		trainingSetPortion=(double)(j)/10;
	    	    		System.out.println(trainingSetPortion);
		    	    	for (int i=0; i<3;i++)
		    	    	{
		    	    		*/
		    	    		modelPath = ((DecisionTreeClassifier)classifier).train(folder+"Transaction"+transactionType+"/LabeledPoints"+min+transactionType+".csv",';',trainingSetPortion,maxDepth,map,context);
		    	    		String modelToDebug = classifier.getModelTodebug();
		    	    		System.out.println("Le modele obtenu est le suivant :\n"+modelToDebug);
		    	    		bw.write(trainingSetPortion+";"+maxDepth+";"+classifier.getTestError()+";"+classifier.getPrecision()+";"+classifier.getRappel());
		    				bw.newLine();
		    	  /*  	}
	    	    	}
    	    	}*/
	    }
	    else
	    {
		classifier = new RandomForestClassifier("frauDetectionApp","local[*]");
		bw.write("TrainingProportion;depth;numTrees;error;precision;recall");
		bw.newLine();
	    	PredictionContext pContext = new PredictionContext();
	    	pContext.setClassifier(classifier);
	    	/*for(int l=2;l<10;l++)
	    	{
	    		numTrees=l;
		    	for(int k = 3;k<5;k++)
		    	{
		    		maxDepth = k;
	    	    	for(int j=1;j<10;j++)
	    	    	{
	    	    		trainingSetPortion=(double)(j)/10;
				    	*/
				    		modelPath = ((RandomForestClassifier)classifier).train(folder+"Transaction"+transactionType+"/LabeledPoints"+min+transactionType+".csv",';',trainingSetPortion,maxDepth,map,2,context);
				    		String modelToDebug = classifier.getModelTodebug();
				    		System.out.println("Le modele obtenu est le suivant :\n"+modelToDebug);
				    		bw.write(trainingSetPortion+";"+maxDepth+";"+numTrees+";"+classifier.getTestError()+";"+classifier.getPrecision()+";"+classifier.getRappel());
							bw.newLine();
				    	
	    	  /*  	}
		    	}
	    	}*/
	    }
	    	classifier.getJsc().close();
			bw.close();
        	
        //etape de prédiction
    		
    	/*   Predictor predictor = new Predictor("frauDetectionApp","local[*]");
    	   predictor.predict(modelPath, "src/main/resources/data/test.csv", ',',context,pContext);
    		*/
    	} catch (IOException e) {
    	    // TODO Auto-generated catch block
    	    e.printStackTrace();
	}
   }
}
