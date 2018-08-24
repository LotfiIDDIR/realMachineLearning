package fr.ensma.lias.bimedia2018machinelearning.generation.utilities;

import java.util.List;
import java.util.Random;

/**
 * @author Lotfi IDDIR 
 */
public class GenerNum {
	
	public int generFromInterval(int min,int max)
	{
		Random random = new Random();
		return random.nextInt(max) + min;
	}
	
	public Float generFloat(float min,float max)
	{
		Random random = new Random();
		return (min+(random.nextFloat()*(max-min)));
	}
	
	public long generLong(long min,long max)
	{	
		return (min+(long)(Math.random()*(max-min)));
	}
	public double generDouble(double min,double max)
	{	
		return (min+(Math.random()*(max-min)));
	}
	public int generFromList(List<String>liste)
	{
		int index = this.generFromInterval(0, liste.size());
		return Integer.parseInt(liste.get(index));
	}

}
