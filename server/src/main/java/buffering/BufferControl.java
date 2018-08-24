package fr.ensma.lias.bimedia2018machinelearning.buffering;

import fr.ensma.lias.bimedia2018machinelearning.engine.Engine;
import fr.ensma.lias.bimedia2018machinelearning.model.Buffer;
import fr.ensma.lias.bimedia2018machinelearning.model.Transaction;

/**
 * @author Lotfi IDDIR
 */
public class BufferControl {

	public void addBuffer(Buffer b, Engine engine) {
		engine.getBuffers().add(b);
	}

	public void deleteBuffer(Buffer b, Engine engine) {
		engine.getBuffers().remove(b);
		b = null;
	}

	public void updateBuffer(Buffer b1, Buffer b2) {
		b1 = b2;
	}

	public Buffer containsPDV(Transaction transact, Engine engine) {
		Buffer found = null;
		if (!engine.getBuffers().isEmpty()) {
			for (Buffer elem : engine.getBuffers()) {
				if (elem.getPdv() == (transact.getPdv())) {
					found = elem;
				}
			}
		}
		return found;
	}

	public void bufferize(Transaction elem, long interval, Engine engine) {
		Buffer last = this.containsPDV(elem, engine);
		// System.out.println(last);
		if (last != null) {
			last.merge(elem, interval);
		} else {
			Buffer b = new Buffer();
			b.getTransactions().add(elem);
			b.setPdv(elem.getPdv());
			b.setPdvCluster(engine.getPdvClusteres().get(elem.getPdv()));
			b.setQuantity();
			b.setDateDebut();
			b.setMontant();
			this.addBuffer(b, engine);
		}
	}
}
