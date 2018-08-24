package fr.ensma.lias.bimedia2018machinelearning.fraudchecker;

import java.io.IOException;

import fr.ensma.lias.bimedia2018machinelearning.model.Buffer;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;
import fr.ensma.lias.bimedia2018machinelearning.prediction.Predictor;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class RuleChecker {

	private Predictor predictor;

	public Predictor getPredictor() {
		return predictor;
	}

	public void setPredictor(Predictor predictor) {
		this.predictor = predictor;
	}

	public void initPredictor(PredictionContext ctx, String pathModel) {
		ctx.getClassifier().setJsc(predictor.getJsc());
		ctx.getClassifier().setModel(pathModel);
	}

	public double predict(Buffer b, PredictionContext ctx) throws IOException {
		return predictor.predict(b.toVector(), ctx);
	}
}
