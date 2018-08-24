package fr.ensma.lias.bimedia2018machinelearning.prediction;

import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;

import scala.Serializable;

/**
 * @author Lotfi IDDIR
 */
public class DecisionTreeClassifier extends Classifier implements Serializable {

	private static final long serialVersionUID = -402594394166381154L;

	private DecisionTreeModel model;

	public DecisionTreeModel getModel() {
		return model;
	}

	public void setModel(DecisionTreeModel model) {
		this.model = model;
	}

	public DecisionTreeClassifier(String appName, String master) {
		super.initClassifier(appName, master);
	}

	public DecisionTreeClassifier() {
	}

	@Override
	public double predict(Vector v) {
		return model.predict(v);
	}

	@Override
	public void setModel(String path) {
		this.setModel(DecisionTreeModel.load(jsc.sc(), path));
	}
}
