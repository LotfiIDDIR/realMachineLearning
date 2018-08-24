package fr.ensma.lias.bimedia2018machinelearning.balancing;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.balancing.KNN;
import fr.ensma.lias.bimedia2018machinelearning.generation.utilities.GenerNum;

/**
 * @author Lotfi IDDIR 
 */
public class GenerLabeledPoints {
	
	//This function is an implemantation of SMOTE approach
	//It generates num points between the given "point" and each of  its "k" nearest neighboors
	public List<LabeledPoint> generRandomNeighboors(List<LabeledPoint>inputList,LabeledPoint point,int k,int num)
	{
		List<LabeledPoint> output = new ArrayList<LabeledPoint>();
		KNN knn = new KNN();
		List<LabeledPoint> kNearestNeighboors = knn.findKNearestNeighbors(inputList, point, k);
		for (LabeledPoint elem : kNearestNeighboors)
		{
			for(int i=0;i<num;i++)
			{output.add(this.generLabeledPoint(point, elem));}
		}
		return output;
		
	}
	
	public LabeledPoint generLabeledPoint(LabeledPoint a, LabeledPoint b)//Function that generates a random labaledPoint between a and b
	{
		double random = new GenerNum().generDouble(0.0, 1.0);
		double label = a.label();
		int numOfAttributes = a.features().size();
		double[] features = new double[numOfAttributes];
		
		for(int i = 0; i < numOfAttributes; i ++){
			features[i]=random*(b.features().apply(i) - a.features().apply(i))+a.features().apply(i);
		}
		
		Vector vector = new DenseVector(features);
		LabeledPoint result = new LabeledPoint(label, vector);
		return result;
		
	}
	
	

}
