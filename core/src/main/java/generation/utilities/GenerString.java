package fr.ensma.lias.bimedia2018machinelearning.generation.utilities;

import java.util.Random;

/**
 * @author Lotfi IDDIR 
 */
public class GenerString {
	
	private Random random;
	
	public char nextChar()
	{
		random = new Random();
		return (char)(random.nextInt(26)+97);
	}
	
public String nextString()
	
	{
		GenerNum gener = new GenerNum();
		int length = gener.generFromInterval(1, 50);
		String str="";
		for (int i=0;i<length;i++)
		{
			char c = nextChar();
			str += c;
		}
		return str;
	}
	
	public String nextString(int length)
	
	{
		String str="";
		for (int i=0;i<length;i++)
		{
			char c = nextChar();
			str += c;
		}
		return str;
	}
	public String nextString(String alphabet)
	
	{
		GenerNum gener = new GenerNum();
		int length = gener.generFromInterval(1, 50);
		String str="";
		int ind = alphabet.length();
		for (int i=0;i<length;i++)
		{
			int k = gener.generFromInterval(0, ind);
			char c = alphabet.charAt(k);
			str += c;
		}
		return str;
	}
	
	public String nextString(String alphabet,int length)
	
	{
		GenerNum gener = new GenerNum();
		String str="";
		int ind = alphabet.length();
		for (int i=0;i<length;i++)
		{
			int k = gener.generFromInterval(0, ind);
			char c = alphabet.charAt(k);
			str += c;
		}
		return str;
	}

}
