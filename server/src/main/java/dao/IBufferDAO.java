package fr.ensma.lias.bimedia2018machinelearning.dao;

import java.util.Set;

import fr.ensma.lias.bimedia2018machinelearning.model.Buffer;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public interface IBufferDAO {
    
    Set<Buffer> getBuffers(int limit);
}
