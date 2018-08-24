package fr.ensma.lias.bimedia2018machinelearning.services;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import fr.ensma.lias.bimedia2018machinelearning.api.SimulationResource;
import fr.ensma.lias.bimedia2018machinelearning.engine.Engine;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class SimulationResourceImpl implements SimulationResource {

	@Inject
	Engine engine;

	@Override
	public void start(int dayInMinutes) {
		System.out.println("SimulationResourceImpl.start()");
		
		if (!engine.isStarted()) {
			engine.setMinutesDuration(dayInMinutes);
			engine.start();
		} else {
			System.out.println("Already started.");
		}
	}

	@Override
	public void stop() {
		System.out.println("SimulationResourceImpl.stop()");
		
		engine.stop();
	}

	@Override
	public String getTime() {
		
		System.out.println(engine.getRealDate());
		//engine.getRealDate();
		return new SimpleDateFormat("HH:mm").format(new Date(engine.getRealDate().getTime()));
	}
	
}
