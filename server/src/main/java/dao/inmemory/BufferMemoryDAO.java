package fr.ensma.lias.bimedia2018machinelearning.dao.inmemory;

import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.dao.IBufferDAO;
import fr.ensma.lias.bimedia2018machinelearning.engine.Engine;
import fr.ensma.lias.bimedia2018machinelearning.model.Buffer;

/**
 * @author Lotfi IDDIR
 */
public class BufferMemoryDAO implements IBufferDAO {

	@Inject
	Engine engine;

	@Override
	public Set<Buffer> getBuffers(int limit) {
		Set<Buffer> set = new TreeSet<Buffer>();
		int i = 0;
		if (limit < 0) {
			for (Buffer elem : engine.getBuffers()) {
				if (i < limit)
					set.add(elem);
				i++;
			}
		} else
			set = engine.getBuffers();
		return set;
	}

}
