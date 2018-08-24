package fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite;

/**
 * @author IDDIR Lotfi
 */
public interface ICSV {
	
	public abstract ICSV readCSV(String[] line,boolean treated);
	public abstract String writeCSV(char separator);


}
