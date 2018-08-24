package fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author IDDIR Lotfi
 */
public class CSVUsage {

	private String path;
	private File file;
	private char separator;
	private CSVReader csvreader;
	
// Getters and Setters
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	public CSVUsage() {
	    super();
	}

	public CSVReader getCsvreader() {
		return csvreader;
	}

	public void setCsvreader(CSVReader csvreader) {
		this.csvreader = csvreader;
	}

	//Constructor
	public CSVUsage(String path, char separator) {
		super();
		this.path = path;
		this.separator = separator;
		this.file = new File(path);
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(isr, 25000000);
			this.csvreader = new CSVReader(br,separator);	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean contains(String[] strings, int size, String newString) {
		boolean exist = false;
		for (int i = 0; i < size; i++) {
			if (strings[i].equals(newString)) {
				exist = true;
			}
		}
		return exist;
	}
	// Function that reads only one column of a CSVFile
	public ArrayList<String> readColumn(int colIndex,boolean withHeader) throws IOException
	{
		String[] nextLine = null;
		ArrayList<String> strings = new ArrayList<String>();
		while ((nextLine = csvreader.readNext()) != null) 
		{
			if(!withHeader)
			{strings.add(nextLine[colIndex]);}
			else {withHeader = false;}
		}

		return strings;
	}
	
	//Function that reads a time column from a CSV File
	public ArrayList<Timestamp> readTime(int colIndex) throws IOException {
		String[] nextLine = null;

		ArrayList<Timestamp> dates = new ArrayList<Timestamp>();
		while ((nextLine = csvreader.readNext()) != null) {
			dates.add(Timestamp.valueOf(nextLine[colIndex]));
		}

		return dates;
	}

	//Function that reads the CSV File and store it on the List of the controller
	public void read(ICSVControl controller,boolean treated,boolean withHeader) throws IOException {
		String[] nextLine = null;
		
		while ((nextLine = csvreader.readNext()) != null) {
			if(!withHeader)
			{controller.addCSVObject(nextLine,treated);}
			else {withHeader = false;}
		}
	}
	
	//Function that writes the List of the Controller on a CSV File
	public void write(String pathDest,ICSVControl controller) throws IOException {
		FileWriter fw = new FileWriter(pathDest,true);
		BufferedWriter bw = new BufferedWriter(fw,250000000);
		List<String>liste = controller.writeCSVObject(separator);
		for(String elem : liste)
		{
			bw.write(elem);
			//if(!elem.equals(liste.get(liste.size()-1)))
			bw.newLine();
		}
		bw.close();
	}
}
