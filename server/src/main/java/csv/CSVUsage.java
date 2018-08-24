package fr.ensma.lias.bimedia2018machinelearning.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author Lotfi IDDIR
 * @author Mickael BARON
 */
public class CSVUsage {

	private String path;

	private File file;

	private char separator;

	private CSVReader csvreader;

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

	public CSVReader getCsvreader() {
		return csvreader;
	}

	public void setCsvreader(CSVReader csvreader) {
		this.csvreader = csvreader;
	}

	public CSVUsage(String path, char separator) {
		super();
		this.path = path;
		this.separator = separator;
		this.file = new File(path);
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(isr, 25000000);
			this.csvreader = new CSVReader(br, separator);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
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

	/**
	 * Function that reads only one column of a CSVFile
	 * 
	 * @param colIndex
	 * @param withHeader
	 * @return
	 * @throws IOException
	 */
	public List<String> readColumn(int colIndex, boolean withHeader) throws IOException {
		String[] nextLine = null;
		ArrayList<String> strings = new ArrayList<String>();
		while ((nextLine = csvreader.readNext()) != null) {
			if (!withHeader) {
				strings.add(nextLine[colIndex]);
			} else {
				withHeader = false;
			}
		}

		return strings;
	}
}
