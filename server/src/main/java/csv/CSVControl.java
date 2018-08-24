package fr.ensma.lias.bimedia2018machinelearning.csv;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.engine.Engine;
import fr.ensma.lias.bimedia2018machinelearning.model.Transaction;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class CSVControl {

	@Inject
	Engine engine;

	public Set<Transaction> loadTransactions() {
		String folder = System.getProperty("user.dir");
		CSVUsage csv = new CSVUsage(folder + "/src/main/resources/PCSResources/PCSTransactions2017-05-25.csv",
				';');
		String[] nextLine = null;
		Set<Transaction> out = new TreeSet<Transaction>();
		try {
			while ((nextLine = csv.getCsvreader().readNext()) != null) {
				Transaction pcs = new Transaction();
				pcs.setPdv(Integer.parseInt(nextLine[2]));
				pcs.setDateDebut(Timestamp.valueOf(nextLine[6]));
				pcs.setQuantity(1);
				pcs.setMontant(Double.parseDouble(nextLine[5]));
				pcs.setFraud(Integer.parseInt(nextLine[7]));
				out.add(pcs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
}
