package fr.ensma.lias.bimedia2018machinelearning.learning;

import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Vector;
//import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionContext;
import scala.Serializable;
import scala.Tuple2;

/**
 * @author Lotfi IDDIR 
 */
public class DecisionTreeClassifier extends Classifier implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -402594394166381154L;
	private DecisionTreeModel model;
	
	//Getters and Setters
	public DecisionTreeModel getModel() {
		return model;
	}

	public void setModel(DecisionTreeModel model) {
		this.model = model;
	}
	//Constructer
	public DecisionTreeClassifier(String appName, String master) {
		super.initClassifier(appName, master);
	}
	
	public DecisionTreeClassifier() {
	    
	}
	
	public String train(String path, char separator,double trainingSetPortion,int maxDepth,Map<Integer, Integer> categoricalFeaturesInfo,TransactionContext context)
	{
	    JavaRDD<LabeledPoint> data =this.createRDD(path, separator,context);// Change CreateRDD
	    JavaRDD<LabeledPoint>[] splits = this.split(data, trainingSetPortion);
            JavaRDD<LabeledPoint> trainingData = splits[0];
            JavaRDD<LabeledPoint> testData = splits[1];
            int numClasses = 2;
            String impurity = "gini";
            int maxBins = 32;
            
            DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses,
              categoricalFeaturesInfo, impurity, maxDepth, maxBins);
            JavaPairRDD<Object, Object> predictionAndLabel =
                    testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
            double testErr = predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testData.count();
            this.setTestError(testErr);
            String output = "tmp/myDecisionTreeClassificationModel"+System.currentTimeMillis();
            //model.save(this.getJsc().sc(), output);
          
         
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
		this.setModel(DecisionTreeModel
		          .load(jsc.sc(), path));
	}

}
