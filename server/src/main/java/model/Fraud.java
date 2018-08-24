package fr.ensma.lias.bimedia2018machinelearning.model;

/**
 * @author Lotfi IDDIR
 */
public class Fraud {
    
    	private int pdv;
    	private double time;

	private String log;

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String toString() {
		return this.log;
	}

	public int getPdv() {
	    return pdv;
	}

	public void setPdv(int pdv) {
	    this.pdv = pdv;
	}

	public double getTime() {
	    return time;
	}

	public void setTime(double time) {
	    this.time = time;
	}
}
