package fr.ensma.lias.bimedia2018machinelearning;

import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.ensma.lias.bimedia2018machinelearning.learning.Classifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.DecisionTreeClassifier;
import fr.ensma.lias.bimedia2018machinelearning.learning.RandomForestClassifier;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionContext;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Transaction;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.TransactionMoneyGram;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.TransactionPCS;


/**
 * @author Lotfi IDDIR 
 */
public class LearningApp {
	
	public static void main( String[] args ) 
    {
	Options options = new Options();
    	options.addOption("trainingSetPortion", false, "Specifie le ratio du dataset utilise pour l'entrainement valeur entre 0.0 et 1.0"); 
    	options.addOption("maxDepth", false, "Specifie la profondeur maximale de l'arbre de decision"); 
    	options.addOption("dataPath", false, "Specifie la profondeur maximale de l'arbre de decision"); 
    	options.addOption("typeTransaction", false, "Specifie le type de transaction fraudée MG pour MoneyGram et PCS pour PCS");
    	CommandLineParser parser = new DefaultParser();
    	HelpFormatter formatter = new HelpFormatter();
    	double trainingSetPortion = 0.6;
    	String folder=System.getProperty("user.dir");
    	folder+="/src/main/resources/";
    	String transactionType="PCS";
    	String predictionModel="DT";
    	Transaction t;
    	int maxDepth=5;
    	String dataPath=folder+"Transaction"+transactionType+"/LabeledPoints10"+transactionType+".csv";
    	try
    	{
    	    CommandLine line = parser.parse(options, args);
    	    if(line.hasOption("trainingSetPortion"))
    	    {
    		trainingSetPortion=Double.parseDouble(line.getOptionValue("trainingSetPortion"));
    	    }
    	    if(line.hasOption("maxDepth"))
    	    {
    		maxDepth=Integer.parseInt(line.getOptionValue("maxdepth"));
    	    }
    	    if(line.hasOption("dataPath"))
    	    {
    		dataPath=line.getOptionValue("dataPath");
    	    }
    	    if(line.hasOption("typeTransaction"))
    	    {
    		transactionType=line.getOptionValue("typeTransaction");
    	    }
    	    if(line.hasOption("predictionModel"))
	    {
  		predictionModel=line.getOptionValue("predictionModel");
	    }
    	    
    	}catch(ParseException e)
    	{
    	    formatter.printHelp("fraudApplication", options);
    	}
		
	// Etape d'apprentissage
    	if (transactionType.equals("PCS"))
    	{
    	    t = new TransactionPCS();
    	}
    	else
    	{
    	    t = new TransactionMoneyGram();
    	}
    	TransactionContext context = new TransactionContext(t);
    	
        Classifier classifier;
	    String modelPath;
	if (predictionModel.equals("DT"))
	    {
	    	classifier = new DecisionTreeClassifier("frauDetectionApp","local[*]");
	    	PredictionContext pContext = new PredictionContext();
	    	pContext.setClassifier(classifier);
	    	modelPath = ((DecisionTreeClassifier)classifier).train(dataPath,';',trainingSetPortion,maxDepth,new HashMap<>(),context);
	    	
	    }
	    else
	    {
		classifier = new RandomForestClassifier("frauDetectionApp","local[*]");
	    	PredictionContext pContext = new PredictionContext();
	    	pContext.setClassifier(classifier);
	    	modelPath = ((RandomForestClassifier)classifier).train(dataPath,';',trainingSetPortion,maxDepth,new HashMap<>(),7,context);

	    }
		String modelToDebug = classifier.getModelTodebug();
		classifier.getJsc().close();
		System.out.println("Le modele obtenu est le suivant :\n"+modelToDebug);   
    }

}
