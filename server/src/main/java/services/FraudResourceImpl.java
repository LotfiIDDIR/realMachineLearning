package fr.ensma.lias.bimedia2018machinelearning.services;

import java.util.List;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.api.FraudResource;
import fr.ensma.lias.bimedia2018machinelearning.dao.IFraudDAO;
import fr.ensma.lias.bimedia2018machinelearning.dto.FraudDTO;
import fr.ensma.lias.bimedia2018machinelearning.model.DTOFactory;
import fr.ensma.lias.bimedia2018machinelearning.model.Fraud;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class FraudResourceImpl implements FraudResource {

	@Inject
	IFraudDAO refFraudDAO;

	@Override
	public List<FraudDTO> getFrauds() {
		final List<Fraud> frauds = refFraudDAO.getFrauds();
		
		return DTOFactory.createFraudsDTO(frauds);
	}
}
