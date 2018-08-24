package fr.ensma.lias.bimedia2018machinelearning.services;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.api.ProductResource;
import fr.ensma.lias.bimedia2018machinelearning.dao.IProductDAO;
import fr.ensma.lias.bimedia2018machinelearning.dto.ProductDTO;
import fr.ensma.lias.bimedia2018machinelearning.model.DTOFactory;
import fr.ensma.lias.bimedia2018machinelearning.model.Product;

/**
 * @author Lotfi IDDIR
 */
public class ProductResourceImpl implements ProductResource {

	@Inject
	IProductDAO refProductDAO;

	@Override
	public List<ProductDTO> getProductsByPDV(int pdv) {
		final List<Product> productsByPDV = refProductDAO.getProductsByPDV(pdv);
			
		return DTOFactory.createProductsDTO(productsByPDV);			
	}

	@Override
	public List<ProductDTO> getSuggestedProductsByPDV(int pdv, Set<String> list) {
		final List<Product> suggestedProductsByPDV = refProductDAO.getSuggestedProductsByPDV(pdv, list);
		
		return DTOFactory.createProductsDTO(suggestedProductsByPDV);
	}
}
