package fr.ensma.lias.bimedia2018machinelearning.generation;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.mllib.regression.LabeledPoint;

/**
 * @author Lotfi IDDIR 
 */
public class KNN {
	
	public double getDistance(LabeledPoint s, LabeledPoint e) // Returns the Euclidean distance between s and e
	{
		assert s.features().size() == e.features().size(): "s and e are different types of LabeledPoints!";
		int numOfAttributes = s.features().size();
		double sum2 = 0;
		
		for(int i = 0; i < numOfAttributes; i ++){
			sum2 += Math.pow(s.features().apply(i) - e.features().apply(i), 2);
		}
		return Math.sqrt(sum2);
	}
	
	public List<LabeledPoint> findKNearestNeighbors(List<LabeledPoint> liste, LabeledPoint point,int k)// returns k nearest neighboors of the given labeledPoint
	{
		int NumOfTrainingSet = liste.size();
		assert k <= NumOfTrainingSet : "K is lager than the length of trainingSet!";
		List<LabeledPoint> output = new ArrayList<LabeledPoint>();
		double [] distances = new double[k];
		
		//initialization, put the first K trainRecords into the above arrayList
				int index;
				for(index = 0; index < k; index++){
					distances[index] = this.getDistance(liste.get(index), point);
					output.add(liste.get(index));
				}
				
		//go through the remaining records in the trainingSet to find K nearest neighbors
		for(index = k; index < NumOfTrainingSet; index ++)
		{
			double distance = this.getDistance(liste.get(index), point);
						
			//get the index of the neighbor with the largest distance to testRecord
			int maxIndex = 0;
			for(int i = 1; i < k; i ++)
			{
				if(distances[i] > distances[maxIndex])
					maxIndex = i;
			}
					
			//add the current trainingSet[index] into neighbors if applicable
			if(distances[maxIndex] > distance)
				distances[maxIndex] = distance;
				output.set(maxIndex, liste.get(index));
		}
		
		return output;
		
	}

}
 