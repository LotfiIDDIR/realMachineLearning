package fr.ensma.lias.bimedia2018machinelearning.learning;

import org.apache.spark.mllib.regression.LabeledPoint;

/**
 * @author Lotfi IDDIR 
 */
public interface Iclassification {
	public LabeledPoint convertToLabeledPoint();
	public Iclassification convertToIclassification(LabeledPoint point);

}
