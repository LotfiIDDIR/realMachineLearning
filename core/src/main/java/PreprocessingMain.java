package fr.ensma.lias.bimedia2018machinelearning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.POSControl;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;

/**
 * @author  Lotfi IDDIR
 */

public class PreprocessingMain {
    
    public static void main( String[] args ) 
    {
	/*String test = "6.5699191E7";
	System.out.println(Double.parseDouble(test));
	System.out.println(new Timestamp((long) Double.parseDouble(test)));*/
    CSVUsage csv = new CSVUsage("C:\\Users\\ASUS\\exportMG\\storesToClustering.csv",';');
    POSControl control = new POSControl();
    try {
	    control.createPointsOfSale("C:\\Users\\ASUS\\exportMG\\storesToClustering.csv",';', false, false);
	    control.writeCSVObject(';');
	    
	    List<String> list = control.getPointsOfSaleToClustering(';');
	    FileWriter fw = new FileWriter("C:\\Users\\ASUS\\exportMG\\storesbin.csv",true);
		BufferedWriter bw = new BufferedWriter(fw,250000000);
	    for (String elem : list)
	    {
	    	bw.write(elem);
	    	bw.newLine();
	    }
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    
	
    }

}
