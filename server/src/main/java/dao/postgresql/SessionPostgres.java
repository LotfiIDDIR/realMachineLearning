package fr.ensma.lias.bimedia2018machinelearning.dao.postgresql;

import static fr.ensma.lias.bimedia2018machinelearning.Bimedia2018MachineLearningConstant.POSTGRESQL_URL_ENV_KEY;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Singleton;

/**
 * @author Lotfi IDDIR
 */
@Singleton
public class SessionPostgres implements IPostgreSession {

	protected Connection cnx;

	@Override
	public Connection getPostgresConnection() {
		return cnx;
	}

	public SessionPostgres() {
		// Check if environment variables exist.
		String postgresDBUrl = this.getPostgresDBUrl();

		try {
			cnx = DriverManager.getConnection(postgresDBUrl, "postgres", "jo2020or");
		} catch (SQLException e) {
			e.printStackTrace();
			cnx = null;
		}
	}

	protected String getPostgresDBUrl() {
		String postgresDBUrl = this.getPostgresDBUrlFromSystem();

		if (postgresDBUrl == null) {
			postgresDBUrl = "jdbc:postgresql://localhost:5432/recommandation";
		}

		return postgresDBUrl;
	}

	protected String getPostgresDBUrlFromSystem() {
		return System.getenv(POSTGRESQL_URL_ENV_KEY);
	}

	public void setCnx(Connection cnx) {
		this.cnx = cnx;
	}

}
