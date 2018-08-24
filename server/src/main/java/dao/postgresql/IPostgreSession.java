package fr.ensma.lias.bimedia2018machinelearning.dao.postgresql;

import java.sql.Connection;

import fr.ensma.lias.bimedia2018machinelearning.dao.ISession;

/**
 * @author Lotfi IDDIR
 */
public interface IPostgreSession extends ISession {
    
    Connection getPostgresConnection();
  
}
