package fr.ensma.lias.bimedia2018machinelearning.dao;

import java.util.List;

import fr.ensma.lias.bimedia2018machinelearning.model.Fraud;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public interface IFraudDAO {
    
    List<Fraud> getFrauds();
}
