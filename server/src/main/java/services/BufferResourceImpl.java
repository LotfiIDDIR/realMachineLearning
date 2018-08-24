package fr.ensma.lias.bimedia2018machinelearning.services;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.api.BufferResource;
import fr.ensma.lias.bimedia2018machinelearning.dao.IBufferDAO;
import fr.ensma.lias.bimedia2018machinelearning.dto.BufferDTO;
import fr.ensma.lias.bimedia2018machinelearning.model.Buffer;
import fr.ensma.lias.bimedia2018machinelearning.model.DTOFactory;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class BufferResourceImpl implements BufferResource {

	@Inject
	IBufferDAO refBuffeurDAO;

	@Override
	public List<BufferDTO> getBuffers(int limit) {
		final Set<Buffer> buffers = refBuffeurDAO.getBuffers(limit);
		
		return DTOFactory.createBuffersDTO(buffers);
	}
}
