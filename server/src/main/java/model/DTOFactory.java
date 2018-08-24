package fr.ensma.lias.bimedia2018machinelearning.model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.TreeSet;

import fr.ensma.lias.bimedia2018machinelearning.dto.BufferDTO;
import fr.ensma.lias.bimedia2018machinelearning.dto.FraudDTO;
import fr.ensma.lias.bimedia2018machinelearning.dto.ProductDTO;
import fr.ensma.lias.bimedia2018machinelearning.dto.TransactionDTO;

/**
 * @author Mickael BARON
 */
public class DTOFactory {

	public static BufferDTO createBufferDTO(Buffer buffer) {
		BufferDTO newBufferDTO = new BufferDTO();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
	    	Date date = new Date (buffer.getDateDebut().getTime());
		newBufferDTO.setDateDebut(dateFormat.format(date));
		newBufferDTO.setMontant(buffer.getMontant());
		newBufferDTO.setPdv(buffer.getPdv());
		newBufferDTO.setQuantite(buffer.getQuantite());
		newBufferDTO.setTotal(buffer.getTotal());
		
		return newBufferDTO;
	}
	
	public static List<BufferDTO> createBuffersDTO(Set<Buffer> buffers) {
		if (buffers != null) {
			List<BufferDTO> newBuffer = new ArrayList<BufferDTO>();
			Iterator<Buffer> iterator = ((TreeSet<Buffer>) buffers).descendingIterator();
				while (iterator.hasNext()) {
    				    newBuffer.add(DTOFactory.createBufferDTO(iterator.next()));
				}
			return newBuffer;
		} else {
			return null;
		}
	}
	
	public static List<FraudDTO> createFraudsDTO(List<Fraud> frauds) {
		if (frauds != null) {
			List<FraudDTO> newRoles = new ArrayList<FraudDTO>();
			for (int i = frauds.size();i>0;i--) {
			    newRoles.add(DTOFactory.createFraudDTO(frauds.get(i-1)));	
			}
			return newRoles;
		} else {
			return null;
		}
	}

	public static ProductDTO createProductDTO(Product current) {
		ProductDTO newProductDTO = new ProductDTO();
		newProductDTO.setAmount(current.getAmount());
		newProductDTO.setCode(current.getCode());
		newProductDTO.setDesignation(current.getDesignation());
		newProductDTO.setStockQuantity(current.getStockQuantity());
		
		return newProductDTO;
	}

	public static List<ProductDTO> createProductsDTO(List<Product> products) {
		if (products != null) {
			List<ProductDTO> newProducts = new ArrayList<ProductDTO>();
			for (Product current : products) {
				newProducts.add(DTOFactory.createProductDTO(current));
			}
			return newProducts;
		} else {
			return null;
		}
	}

	public static FraudDTO createFraudDTO(Fraud current) {
		FraudDTO newFraudDTO = new FraudDTO();
		newFraudDTO.setLog(current.getLog());
		
		return newFraudDTO;
	}
	
	public static TransactionDTO createTransactionDTO(Transaction transaction) {
		TransactionDTO newTransactionDTO = new TransactionDTO();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
	    	Date date = new Date (transaction.getDateDebut().getTime());
	    	newTransactionDTO.setDateDebut(dateFormat.format(date));
		newTransactionDTO.setMontant(transaction.getMontant());
		newTransactionDTO.setPdv(transaction.getPdv());
		newTransactionDTO.setQuantity(transaction.getQuantity());
		return newTransactionDTO;
	}

	public static List<TransactionDTO> createTransactionsDTO(Set<Transaction> transactions) {
		if (transactions != null) {
			List<TransactionDTO> newTransactions = new ArrayList<TransactionDTO>();
			Iterator<Transaction> iterator = ((TreeSet<Transaction>) transactions).descendingIterator();
			while (iterator.hasNext()) {
				newTransactions.add(DTOFactory.createTransactionDTO(iterator.next()));
			}
			return newTransactions;
		} else {
			return null;
		}
	}
}
