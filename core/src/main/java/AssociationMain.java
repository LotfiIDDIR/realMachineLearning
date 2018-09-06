package fr.ensma.lias.bimedia2018machinelearning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.AssociationRule;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Result;

/**
 * @author  Lotfi IDDIR
 */
public class AssociationMain {
	private static JavaSparkContext sc;
	public static void main( String[] args ) 
    {
		Options options = new Options();
    	options.addOption("minSup", true, "Specifie le support minimal entre 0.0 et 1.0");  
    	options.addOption("minConf", true, "Specifie la confiance minimale entre 0.0 et 1.0 "); 
    	options.addOption("pdv", true, "Specifie le point de vente"); 
    	CommandLineParser parser = new DefaultParser();
    	HelpFormatter formatter = new HelpFormatter();
    	int pdv=350459;
    	double minSup = 0.001;
    	double minConf = 0.1;
    	String folder=System.getProperty("user.dir");
    	try
    	{
    	    CommandLine line = parser.parse(options, args);
    	    if(line.hasOption("pdv"))
    	    {
    		pdv=Integer.parseInt(line.getOptionValue("pdv"));
    	    }
    	    if(line.hasOption("minSup"))
    	    {
    		minSup=Double.parseDouble(line.getOptionValue("minSup"));
    	    }
    	    if(line.hasOption("minConf"))
    	    {
    		minConf=Double.parseDouble(line.getOptionValue("minConf"));
    	    }
    	}catch(ParseException e)
    	{
    		formatter.printHelp("fraudApplication", options);
    	}
		SparkConf conf = new SparkConf().setAppName("FP-growth Example").setMaster("local[*]");
	    sc = new JavaSparkContext(conf);
	    // $example on$
	    JavaRDD<String> data = sc.textFile("C:\\Users\\ASUS\\Desktop\\ditinctProduct\\data\\data"+pdv+".txt");
	    JavaRDD<List<String>> transactions = data.map(line -> Arrays.asList(line.split(";")));
	    FPGrowth fpg = new FPGrowth()
	        .setMinSupport(minSup)
	        .setNumPartitions(1);
	    int freq = 0;
	    FPGrowthModel<String> model = fpg.run(transactions);
	    for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
	       freq+= itemset.freq();
	      }
	    Result result = new Result();
	    FileWriter fw;
	  	for (AssociationRules.Rule<String> rule
	  			: model.generateAssociationRules(minConf).toJavaRDD().collect()) {
	        AssociationRule assoc = new AssociationRule();
	        assoc.setAntecedent(rule.javaAntecedent());
	        assoc.setConfidence(rule.confidence());
	        assoc.setConsequent(rule.javaConsequent());
	        result.getRules().add(assoc);
	      }
	  	try {
	  		File file = new File(folder+"/rules"+pdv+".json");
	  		if (file.exists())
	  			file.delete();
	  		fw = new FileWriter(folder+"/rules"+pdv+".json",true);
	  		BufferedWriter bw = new BufferedWriter(fw,250000000);
	  		bw.write(result.writeJson());
	  		bw.close();
	  	} catch (IOException e) {
	  		// TODO Auto-generated catch block
	  		e.printStackTrace();
	  	}
	  	sc.close();
	  	System.out.println("nombre de regles obtenu : "+result.getRules().size()+ "\n");
	  	System.out.println("frequence d'apparition moyenne d'un itemset : "+freq/result.getRules().size()+ "\n");
    }
}