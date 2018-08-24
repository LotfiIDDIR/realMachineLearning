package fr.ensma.lias.bimedia2018machinelearning.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import fr.ensma.lias.bimedia2018machinelearning.model.Product;

/**
 * @author Lotfi IDDIR
 */
public interface IProductDAO {

    /**
     * @param pdv
     * @return
     * @throws SQLException 
     */
    List<Product> getProductsByPDV(int pdv);
    
    /**
     * @param pdv
     * @param inputProducts
     * @return
     */
    List<Product> getSuggestedProductsByPDV(int pdv,Set<String>inputProducts);
}
