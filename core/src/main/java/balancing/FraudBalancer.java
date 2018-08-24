package fr.ensma.lias.bimedia2018machinelearning.balancing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TransactionContext;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;

/**
 * @author Lotfi IDDIR 
 */
public class FraudBalancer implements IBalancing {
    
    	List<LabeledPoint> fraudPoints;
	//Getters and Setters
	
	//Constructors
	public FraudBalancer() {
		super();
		this.fraudPoints=new ArrayList<LabeledPoint>();
	}

	public void balance(String path,char separator,double finalRatio,TransactionContext context) throws IOException
	{
		List<LabeledPoint>fraudPoints = new ArrayList<LabeledPoint>();
		fraudPoints=balanceFrauds(path,separator,finalRatio);
		/*for(LabeledPoint point : fraudPoints)
		{
		    System.out.println(point);
		}*/
		FileWriter fw = new FileWriter(path,true);// Persist method in Controler
		BufferedWriter bw = new BufferedWriter(fw,250000000);
		for (LabeledPoint fraud : fraudPoints)
		{
		    bw.write(context.writeLabeledPoint(fraud,separator));
		    bw.newLine();
		}
		bw.close();
	}
	
	public List<LabeledPoint> balanceFrauds(String path,char separator,double newFraudRatio)//Function that changes the ratio of frauds to a new ratio
	{
        	double ratio=0.0;
        	try {
    			ratio=this.extractMinorClass(path, separator);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	List<LabeledPoint>SyntheticFrauds = this.balanceLabeledPoints(fraudPoints, ratio, newFraudRatio);
        	return SyntheticFrauds;
	}
	
	
	public double extractMinorClass (String path,char separator) throws IOException// Function that extracts frauds from the given file
	{
		CSVUsage csv = new CSVUsage(path,separator);
		String[] nextLine = null;
		
		int total=0;
		List<LabeledPoint>output= new ArrayList<LabeledPoint>();
		while ((nextLine = csv.getCsvreader().readNext()) != null) 
		{
			total++;
			if(nextLine[nextLine.length-1].equals("1.0"))
			{
				LabeledPoint fraud = readLabeledPoint(nextLine);
				output.add(fraud);
			}
		}
		fraudPoints=output;
		double ratio = (double)output.size()/(double)total;
		return ratio;
	}
	
	public LabeledPoint readLabeledPoint (String[] nextLine)
	{
	    double label = Double.parseDouble(nextLine[nextLine.length-1]);
	    double[] features = new double[nextLine.length-1];
	    for (int i =0; i < nextLine.length-1;i++)
	    {
		features[i] = Double.parseDouble(nextLine[i]);
	    }
	    Vector vector = new DenseVector(features);
	   return  new LabeledPoint(label,vector);
	}
	
	public String writeLabeledPoint(LabeledPoint point,char separator) {
	    String out="";
	    for(int i =0; i<point.features().size();i++)
	    {
		out+=point.features().apply(i);
		out+=separator;
	    }
	    out+=point.label();
	    return out;
	}
		
	
	@Override
	public List<LabeledPoint> balanceLabeledPoints(List<LabeledPoint> minorClass, double initialRatio, double finalRatio) {
		int k = (int)((finalRatio*(1-initialRatio))/(initialRatio*(1-finalRatio)));
		int num = (k/minorClass.size())+1;
		k=k/num;
		return generlabeledPoints(minorClass,k,num);
	}


	@Override
	public List<LabeledPoint> generlabeledPoints(List<LabeledPoint> inputList, int k, int num) {
		List<LabeledPoint> output = new ArrayList<LabeledPoint>();
		GenerLabeledPoints gener = new GenerLabeledPoints();
		KNN knn = new KNN();
		for(LabeledPoint point : inputList)
		{
			List<LabeledPoint> kNearestNeighboors = knn.findKNearestNeighbors(inputList, point, k);
			for (LabeledPoint elem : kNearestNeighboors)
			{
				for(int i=0;i<num;i++)
				{output.add(gener.generLabeledPoint(point, elem));
				}
			}
		}
		return output;
	}

}
