package fr.ensma.lias.bimedia2018machinelearning.dto;

/**
 * @author Lotfi IDDIR
 */
public class MetricsDTO {
	private double recall;
	private double precision;
	private double error;
	public double getRecall() {
		return recall;
	}
	public void setRecall(double recall) {
		this.recall = recall;
	}
	public double getPrecision() {
		return precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public double getError() {
		return error;
	}
	public void setError(double error) {
		this.error = error;
	}

}
