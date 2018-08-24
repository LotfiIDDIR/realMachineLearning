package fr.ensma.lias.bimedia2018machinelearning;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TicketControl;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.AssociationRule;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.Result;



/**
 * @author IDDIR Lotfi
 *
 */
public class App 
{
	public static void main( String[] args ) 
    {
		TicketControl control = new TicketControl();
		List<Integer> pdvs= new ArrayList<Integer>();
		pdvs.add(350459);
		pdvs.add(20369);
		pdvs.add(22740);
		pdvs.add(29009);
		pdvs.add(29015);
		pdvs.add(29022);
		pdvs.add(30415);
		pdvs.add(59008);
		pdvs.add(62737);
		pdvs.add(69001);
		pdvs.add(100148);
		pdvs.add(130873);
		pdvs.add(149018);
		pdvs.add(169017);
		pdvs.add(179018);
		pdvs.add(180049);
		pdvs.add(180073);
		pdvs.add(189004);
		pdvs.add(189013);
		pdvs.add(200145);
		pdvs.add(240155);
		pdvs.add(259003);
		pdvs.add(259011);
		pdvs.add(260081);
		pdvs.add(260120);
		pdvs.add(260211);
		pdvs.add(260526);
		pdvs.add(279022);
		pdvs.add(279028);
		pdvs.add(279031);
		pdvs.add(280103);
		pdvs.add(280193);
		pdvs.add(290201);
		pdvs.add(299049);
		pdvs.add(299077);
		pdvs.add(299088);
		pdvs.add(310414);
		pdvs.add(310699);
		pdvs.add(330159);
		pdvs.add(340223);
		pdvs.add(340225);
		pdvs.add(340861);
		pdvs.add(350056);
		pdvs.add(359003);
		pdvs.add(370014);
		pdvs.add(370921);
		pdvs.add(420458);
		pdvs.add(439008);
		pdvs.add(440062);
		pdvs.add(440282);
		pdvs.add(440414);
		pdvs.add(440597);
		pdvs.add(450335);
		pdvs.add(450948);
		pdvs.add(470268);
		pdvs.add(480046);
		pdvs.add(490414);
		pdvs.add(560219);
		pdvs.add(561320);
		pdvs.add(579005);
		pdvs.add(590857);
		pdvs.add(599019);
		pdvs.add(600247);
		pdvs.add(609005);
		pdvs.add(620215);
		pdvs.add(621147);
		pdvs.add(629022);
		pdvs.add(629024);
		pdvs.add(640071);
		pdvs.add(650135);
		pdvs.add(670447);
		pdvs.add(680059);
		pdvs.add(689008);
		pdvs.add(699011);
		pdvs.add(700109);
		pdvs.add(740009);
		pdvs.add(749002);
		pdvs.add(759039);
		pdvs.add(780464);
		pdvs.add(790171);
		pdvs.add(809027);
		pdvs.add(810307);
		pdvs.add(830014);
		pdvs.add(840232); 
		pdvs.add(859013);
		pdvs.add(860540);
		pdvs.add(880495);
		pdvs.add(899021);
		pdvs.add(910474);
		pdvs.add(920138);
		pdvs.add(920204);
		pdvs.add(920839);
		pdvs.add(939013);
		pdvs.add(940132);
		pdvs.add(940267);
		pdvs.add(940279);
     SparkConf conf = new SparkConf().setAppName("FP-growth Example").setMaster("local[*]");
     JavaSparkContext sc = new JavaSparkContext(conf);

      // $example on$
     for (int pdv : pdvs) {
    	 String path = "C:\\Users\\ASUS\\Desktop\\PFE\\Rapport\\Statistiques\\rules\\rules.csv";
   	    FileWriter fw;
 		try {
 			fw = new FileWriter(path,true);
 		
   		BufferedWriter bw = new BufferedWriter(fw,250000000);
   		bw.write("pdv;minsup;freq;minconf;nbrule");
   		bw.newLine();
    	 
      JavaRDD<String> data = sc.textFile("C:\\Users\\ASUS\\Desktop\\ditinctProduct\\data\\data"+pdv+".txt");
      JavaRDD<List<String>> transactions = data.map(line -> Arrays.asList(line.split(";")));
      for (int i = 1;i<200;i=i+5)
      {
    	
    	  double minsup = (double)(i)/1000;
	      FPGrowth fpg = new FPGrowth()
	        .setMinSupport(minsup)
	        .setNumPartitions(1);
	      FPGrowthModel<String> model = fpg.run(transactions);
	      long freq = 0;
	      int total=0;
	      for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
	        //System.out.println(itemset.javaItems() + ", " + itemset.freq());
	    	  freq=freq+itemset.freq();
	    	  total++;
	      }
	      long mean=0;
	      if(total>0)
	    	  mean = freq/total;
	      for (int j = 1;j<6;j++)
	      {
		      double minConfidence = (double)(j)/10;
		 /*     FileWriter fw;
		  	try {
		  		fw = new FileWriter(".\\src\\main\\resources\\rules740009.txt",true);
		  		BufferedWriter bw = new BufferedWriter(fw,250000000);*/
		     // JSONArray array = new JSONArray();
		      List<AssociationRule> rules = new ArrayList<AssociationRule>();
		  		for (AssociationRules.Rule<String> rule
		  		        : model.generateAssociationRules(minConfidence).toJavaRDD().collect()) {
		  		      //  bw.write(rule.javaAntecedent() + "," + rule.javaConsequent());
		  		        //bw.newLine();
		  			AssociationRule assoc = new AssociationRule();
		  			assoc.setAntecedent(rule.javaAntecedent());
		  			assoc.setConsequent(rule.javaConsequent());
		  			assoc.setConfidence(rule.confidence());
		  			rules.add(assoc);
		  			//System.out.println(assoc.writeJson());
		  			//array.add(assoc);
		  		}
		  		bw.write(pdv+";"+minsup+";"+mean+";"+minConfidence+";"+rules.size());
		  		bw.newLine();
	      }
	      
		
      	}bw.close();
  		/*Result r = new Result();
  		 r.setRules(rules);*/
  		// used to pretty print result
  		/* Gson gson = new GsonBuilder().setPrettyPrinting().create();
  		 String strJson = gson.toJson(r);
  		FileWriter writer = null;
  		try {
  		   writer = new FileWriter("C:\\Users\\ASUS\\Desktop\\association\\gen.json");
  		   writer.write(strJson);
  		 } catch (IOException e) {
  		   e.printStackTrace();
  		 } finally {
  		   if (writer != null) {
  		    try {
  		     writer.close();
  		    } catch (IOException e) {
  		     e.printStackTrace();
  		    }
  		   }
  		 }
  			/* Gson gson = new GsonBuilder().setPrettyPrinting().create();
  			 String strJson = gson.toJson(rules);
  			System.out.println(strJson);*/
  	
  		//System.out.println(array.toJSONString());
 		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
 		
  		/*bw.close();
  		      
  		
  	} catch (IOException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
      */
    
		
      // $example off$

      sc.stop();
	
	/*	BufferedReader br = null;
		FileReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(".\\src\\main\\resources\\rules740009.txt");
			br = new BufferedReader(fr);

			String sCurrentLine;
			String productCode="30";

			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.startsWith("["+productCode+"]"))
				{
					String codes = sCurrentLine.substring(productCode.length()+3, sCurrentLine.length()-1);
					System.out.println(codes);
					String [] trueCodes = codes.split(",");
					for (int i =0; i<trueCodes.length;i++)
					{
						
					System.out.println("code"+trueCodes[i].replace("[", ""));
					}
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		
		}*/
		
		
	/*	for(int pdv : pdvs)
		{
		try {
			control.fusion("C:\\Users\\ASUS\\Desktop\\ditinctProduct\\data"+pdv+".csv", "C:\\\\Users\\\\ASUS\\\\Desktop\\\\ditinctProduct\\\\data"+pdv+".txt", ',');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		control = null;
		control = new TicketControl();
		}*/	
      
   /*   Gson gson = new Gson();
      BufferedReader br = null;
     try {
        br = new BufferedReader(new FileReader("C:\\Users\\ASUS\\Desktop\\association\\gen.json"));
        Result result = gson.fromJson(br, Result.class);
        for (AssociationRule rule : result.getRules())
        	System.out.println(rule.writeJson());
     } catch (FileNotFoundException e) {
         e.printStackTrace();
       } finally {
         if (br != null) {
          try {
           br.close();
          } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
          }
       }
      }*/
    }
}
