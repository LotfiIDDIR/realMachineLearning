package fr.ensma.lias.bimedia2018machinelearning.dao.inmemory;

import java.util.List;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.dao.IFraudDAO;
import fr.ensma.lias.bimedia2018machinelearning.engine.Engine;
import fr.ensma.lias.bimedia2018machinelearning.model.Fraud;

/**
 * @author Lotfi IDDIR
 */
public class FraudMemoryDAO implements IFraudDAO {

	@Inject
	Engine engine;

	@Override
	public List<Fraud> getFrauds() {
		return engine.getFrauds();
	}
}
