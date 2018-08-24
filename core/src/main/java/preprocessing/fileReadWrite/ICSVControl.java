package fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite;

import java.util.List;

/**
 * @author IDDIR Lotfi
 */

public interface ICSVControl {
	public abstract void addCSVObject(String[] line,boolean treated);
	public abstract List<String> writeCSVObject(char separator);

}

