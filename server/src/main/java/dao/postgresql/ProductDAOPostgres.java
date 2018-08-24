package fr.ensma.lias.bimedia2018machinelearning.dao.postgresql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.dao.IProductDAO;
import fr.ensma.lias.bimedia2018machinelearning.model.Product;

/**
 * @author Lotfi IDDIR
 */
public class ProductDAOPostgres implements IProductDAO {

	@Inject
	IPostgreSession postgreSession;

	private void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Product> getProductsByPDV(int pdv) {
		Connection cnx = new SessionPostgres().getPostgresConnection();
		List<Product> outList = new ArrayList<Product>();

		Statement st = null;
		try {
			st = cnx.createStatement();
			String query = "select code_barre , designation, unit_price, stockquantity\n" + "\n"
					+ "from products join stock on code_barre=idproduct where pdv = " + pdv;

			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				Product p = new Product();
				p.setAmount(rs.getDouble("unit_price"));
				p.setCode(rs.getString("code_barre"));
				p.setDesignation(rs.getString("designation"));
				p.setStockQuantity(rs.getInt("stockquantity"));
				outList.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(st);
		}

		return outList;
	}

	@Override
	public List<Product> getSuggestedProductsByPDV(int pdv, Set<String> inputProducts) {
		Connection cnx = new SessionPostgres().getPostgresConnection();
		List<Product> outList = new ArrayList<Product>();
		final Set<String> allProducts = this.getAllProducts(pdv, inputProducts);
		System.out.println(allProducts);
		if (allProducts == null || allProducts.isEmpty()) {
			return outList;
		}

		Statement st = null;
		try {
			st = cnx.createStatement();
			String query = "select code_barre , designation, unit_price, stockquantity\n" + "\n"
					+ "from products join stock on code_barre=idproduct where pdv = " + pdv + "\n"
					+ "and code_barre in (";
			int nb = 0;
			for (String product : allProducts) {
				query += "'" + product + "',";
				nb++;
			}
			if (nb > 0)
				query = query.substring(0, query.length() - 1);
			query += ")";

			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				Product p = new Product();
				p.setAmount(rs.getDouble("unit_price"));
				p.setCode(rs.getString("code_barre"));
				p.setDesignation(rs.getString("designation"));
				p.setStockQuantity(rs.getInt("stockquantity"));
				if (p.getStockQuantity() > 0 && !inputProducts.contains(p.getCode())) {
					outList.add(p);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(st);
		}

		return outList;
	}

	public Set<String> getAllProducts(int pdv, Set<String> products) {
		Set<String> outList = new TreeSet<String>();
		BufferedReader br = null;
		FileReader fr = null;

		try {
			String folder = System.getProperty("user.dir");
			fr = new FileReader(folder + "/src/main/resources/products/rules"+pdv+".txt");
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] sCurrentLineParts = sCurrentLine.split(";");
				if (this.isAntecedent(sCurrentLineParts[0], products)) {
					String codes = sCurrentLineParts[1].substring(1, sCurrentLineParts[1].length() - 1);
					String[] trueCodes = codes.split(",");
					for (int i = 0; i < trueCodes.length; i++) {
						String out = "";
						out = trueCodes[i].replace(" ", "");
						outList.add(out);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}

				if (fr != null) {
					fr.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return outList;
	}

	public boolean isAntecedent(String line, Set<String> products) {
		boolean out = false;
		List<String> sublists = this.extractSubsets(products);
		if (sublists.contains(line))
			out = true;

		return out;
	}

	public List<String> extractSubsets(Set<String> products) {
		int n = products.size();
		List<String> productsList = new ArrayList<String>(products);
		List<String> out = new ArrayList<String>();
		for (int i = 0; i < (1 << n); i++) {
			String subset = "[";
			for (int j = 0; j < n; j++)
				if ((i & (1 << j)) > 0)
					subset += productsList.get(j) + ", ";
			if (subset.length() > 1)
				subset = subset.substring(0, subset.length() - 2);
			subset += "]";
			out.add(subset);
		}
		return out;
	}
}
