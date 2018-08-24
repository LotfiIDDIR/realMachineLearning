package fr.ensma.lias.bimedia2018machinelearning.balancing;

import java.util.List;

import org.apache.spark.mllib.regression.LabeledPoint;

/**
 * @author Lotfi IDDIR 
 */

public interface IBalancing {
	
	public List<LabeledPoint>balanceLabeledPoints(List<LabeledPoint>minorClass,double initialRatio,double FinalRatio);// Function that changes the ratio of given minorClass to the expected finalRatio 
	public List<LabeledPoint>generlabeledPoints(List<LabeledPoint>inputList,int k,int num);//Generate using SMOTE implementation
}
