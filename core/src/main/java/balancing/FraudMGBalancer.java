package fr.ensma.lias.bimedia2018machinelearning.balancing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.mllib.regression.LabeledPoint;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.TransactionMoneyGram;

/**
 * @author Lotfi IDDIR 
 */
public class FraudMGBalancer implements IBalancing {
	
	List<TransactionMoneyGram> frauds;//Replace it with Transactioncontroler
	
	//Getters and Setters
	public List<TransactionMoneyGram> getFrauds() {
		return frauds;
	}

	public void setFrauds(List<TransactionMoneyGram> frauds) {
		this.frauds = frauds;
	}
	
	
	//Constructors
	public FraudMGBalancer() {
		super();
		this.frauds=new ArrayList<TransactionMoneyGram>();
	}

	public void balance(String path,char separator,double finalRatio) throws IOException
	{
		List<LabeledPoint>fraudPoints = new ArrayList<LabeledPoint>();
		fraudPoints=balanceFrauds(path,separator,finalRatio);
		//TransactionMoneyGram t = new TransactionMoneyGram();//
		FileWriter fw = new FileWriter(path,true);// Persist method in Controler
		BufferedWriter bw = new BufferedWriter(fw,250000000);
		for (LabeledPoint fraud : fraudPoints)
		{
			for (int i = 0; i<fraud.features().size();i++)
			{
				bw.write(Double.toString((double)Math.round(fraud.features().apply(i))));bw.write(separator);
			}
			bw.write(Double.toString(fraud.label()));
			bw.newLine();
		}
		bw.close();
	}
	
	public List<LabeledPoint> balanceFrauds(String path,char separator,double newFraudRatio)//Function that changes the ratio of frauds to a new ratio
	{
    	double ratio=0.0;
    	try {
			ratio=this.extractFrauds(path,separator);// A modifier
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	List<LabeledPoint>fraudPoints = new ArrayList<LabeledPoint>();
		for (TransactionMoneyGram fraud:frauds )//can use TransactionControler
		{
			fraudPoints.add(fraud.convertToLabeledPoint());
		}
		List<LabeledPoint>SyntheticFrauds = this.balanceLabeledPoints(fraudPoints, ratio, newFraudRatio);
		return SyntheticFrauds;
	}
	
	
	public double extractFrauds (String path,char separator) throws IOException// Function that extracts frauds from the given file
	{
		CSVUsage csv = new CSVUsage(path,separator);
		String[] nextLine = null;
		
		int total=0;
		List<TransactionMoneyGram>output= new ArrayList<TransactionMoneyGram>();
		while ((nextLine = csv.getCsvreader().readNext()) != null) 
		{
			total++;
			if(nextLine[6].equals("1.0"))
			{
				TransactionMoneyGram t = new TransactionMoneyGram();
				TransactionMoneyGram.initCategories();
				t=(TransactionMoneyGram) t.readCSV(nextLine, true);
				output.add(t);
			}
		}
		this.setFrauds(output);
	
		double ratio = (double)output.size()/(double)total;
		System.out.println(ratio);
		return ratio;
	}// HAVE to change place of this
		
	
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
