package fr.ensma.lias.bimedia2018machinelearning.learning;

import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionContext;
import scala.Serializable;
import scala.Tuple2;

/**
 * @author  Lotfi IDDIR
 */

public class RandomForestClassifier extends Classifier implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private RandomForestModel model;
	
	//Getters and Setters
	public RandomForestModel getModel() {
		return model;
	}

	public void setModel(RandomForestModel model) {
		this.model = model;
	}
	//Constructer
	public RandomForestClassifier(String appName, String master) {
		super.initClassifier(appName, master);
	}
	
	public RandomForestClassifier() {
	    
	}
	
	public String train(String path, char separator,double trainingSetPortion,int maxDepth,Map<Integer, Integer> categoricalFeaturesInfo,int numTrees,TransactionContext context)
	{
	    JavaRDD<LabeledPoint> data =this.createRDD(path, separator,context);// Change CreateRDD
	    JavaRDD<LabeledPoint>[] splits = this.split(data, trainingSetPortion);
	    JavaRDD<LabeledPoint> trainingData = splits[0];
	    JavaRDD<LabeledPoint> testData = splits[1];
	    int numClasses = 2;
	    String impurity = "gini";
	    int maxBins = 32;
        
	    RandomForestModel model = RandomForest.trainClassifier(trainingData, numClasses,
		    categoricalFeaturesInfo, numTrees, "auto",impurity, maxDepth, maxBins,1000);
	    JavaPairRDD<Object, Object> predictionAndLabel =
                testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
	    double testErr = predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testData.count();
	    this.setTestError(testErr);
	    String output = "C:\\Users\\ASUS\\Desktop\\PFE\\Demo\\models\\randomForest"+System.currentTimeMillis();
	    this.getJsc().sc().hadoopConfiguration().set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
	    this.getJsc().sc().hadoopConfiguration().set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
	    model.save(this.getJsc().sc(), output);
	    MulticlassMetrics metrics= new   MulticlassMetrics(predictionAndLabel.rdd()); 
	    this.setPrecision(metrics.precision(1.0));
	    this.setRappel(metrics.recall(1.0));
	    this.setModelTodebug(model.toDebugString()+"\n Test error = "+testErr+"\n Recall = "+metrics.recall(1.0)+"\n Precision ="+metrics.precision(1.0)+"\n Table de confusion :\n"+ metrics.confusionMatrix());
	    return output;		
	}

	@Override
	public double predict(Vector v) {
		 return model.predict(v);	
	}

	@Override
	public void setModel(String path) {
		this.setModel(RandomForestModel
		          .load(jsc.sc(), path));
	}
}