package fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSVControl;
import fr.ensma.lias.bimedia2018machinelearning.preprocessing.model.PointOfSale;

/**
 * @author  Lotfi IDDIR
 */

public class POSControl implements ICSVControl{

	private List<PointOfSale> pointsOfSale;
	private KMeansModel kMeansModel;
	private JavaSparkContext jsc;
	private Map<Integer,Integer> clusterMapping;
	
	//Getters and Setters
	
	public List<PointOfSale> getPointsOfSale() {
	    return pointsOfSale;
	}

	public void setPointsOfSale(List<PointOfSale> pointsOfSale) {
	    this.pointsOfSale = pointsOfSale;
	}
	
	public KMeansModel getkMeansModel() {
	    return kMeansModel;
	}

	public void setkMeansModel(KMeansModel kMeansModel) {
	    this.kMeansModel = kMeansModel;
	}
	
	public Map<Integer,Integer> getClusterMapping() {
	    return clusterMapping;
	}

	public void setClusterMapping(Map<Integer,Integer> clusterMapping) {
	    this.clusterMapping = clusterMapping;
	}
	
	public void createPointsOfSale(String path,char separator,boolean withHeader,boolean treated) throws IOException
	{
	    CSVUsage csvreader = new CSVUsage(path,separator);
	    csvreader.read(this, treated,withHeader);//Fills the List of POS from the CSV Files
	}
	
	//Constructors

	public POSControl() {
	    super();
	    this.pointsOfSale=new ArrayList<PointOfSale>();
	    this.clusterMapping= new HashMap<Integer,Integer>();
	}

	@Override
	public void addCSVObject(String[] line, boolean treated) {//Add a POS to the list from a CSV line 
	    PointOfSale pos = new PointOfSale();
	    pos.readCSV(line, treated);
	    pointsOfSale.add(pos);
	}

	@Override
	public List<String> writeCSVObject(char separator) {// Prepare the list to be written on a CSV File
	    List<String>liste = new ArrayList<String>();
	    SparkSession spark = SparkSession
				.builder()
				.appName("JavaCountVectorizerExample")
				.master("local[*]")
				.getOrCreate();
	    for(PointOfSale elem: pointsOfSale)
	    {
		List<Row> data= new ArrayList<Row>();
		data.add( RowFactory.create(elem.getServices()));
		StructType schema = new StructType(new StructField [] {
			new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
		});
		Dataset<Row> df = spark.createDataFrame(data, schema);
		CountVectorizerModel cvm = new CountVectorizerModel(new String[]{"Annonce","BuyBox","CDiscount","CanalPlus","Cash Way","ChaabiDirect","Commande Livraison Tabac","Coyote","Digitick","Hotline","Librairie","Moneygram",
						"MoyensPaiement","Orange Money","Presse","SpiritofCadeau","Telephonie","TimbreAmende","TransferTo","TransferVideo",
						"Vente de produits physiques","Vente produits Compte Nickel","ViaPresse","WonderBox","Yes by Cash"})
			      .setInputCol("text")
			      .setOutputCol("feature");
		Row row= cvm.transform(df).first();
		DenseVector vector = ((SparseVector) row.get(1)).toDense();
		elem.setServicesBin(vector.values());
		liste.add(elem.writeCSV(separator));
	    }
	    spark.close();
	    return liste;
	}
	
	public void ClusKMeans(int k,char separator)
	{
	    // Load and parse data
	    SparkConf sparkConf = new SparkConf().setAppName("KMeansApp").setMaster("local[*]");
	    this.jsc = new JavaSparkContext(sparkConf);
	    JavaRDD<Vector> parsedData = this.getJavaRDD(separator);
	    parsedData.cache();

	    // Cluster the data into two classes using KMeans
	    int numClusters = k;
	    int numIterations = 10;
	    KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);
	    this.setkMeansModel(clusters);
	   
		

	    System.out.println("Cluster centers:");
	    for (Vector center: clusters.clusterCenters()) {
		System.out.println(" " + center);
	    }
	    double cost = clusters.computeCost(parsedData.rdd());
	    System.out.println("Cost: " + cost);

	    // Evaluate clustering by computing Within Set Sum of Squared Errors
	    double WSSSE = clusters.computeCost(parsedData.rdd());
	    System.out.println("Within Set Sum of Squared Errors = " + WSSSE);
	    // Save model
	   // clusters.save(jsc.sc(), "target/KMeansModel");
	    jsc.close();
	}
	
	public List<String>getPointsOfSaleToClustering(char separator)
	{
	    List<String> output = new ArrayList<String>();
	    for (PointOfSale elem : this.getPointsOfSale())
	    {
		String elemString = elem.getPOSToClustering(separator);
		output.add(elemString);
	    }
	    return output;
	}
	
	public JavaRDD<Vector>getJavaRDD(char separator)
	{
	    JavaRDD<String> data = jsc.parallelize(this.getPointsOfSaleToClustering(separator));
	    JavaRDD<Vector> parsedData = data.map(s -> {
		String[] sarray = s.split(separator+"");
		double[] values = new double[sarray.length];
		  for (int i = 0; i < sarray.length; i++) {
		    values[i] = Double.parseDouble(sarray[i]);
		  }
		  return Vectors.dense(values);
		});
	    return parsedData;
	}
	
	public KMeansModel loadModel(String appName, String master,String path)
	{
	    SparkConf sparkConf = new SparkConf().setAppName(appName).setMaster(master);
	    jsc = new JavaSparkContext(sparkConf);
	    KMeansModel sameModel = KMeansModel.load(jsc.sc(),path);
	    //jsc.close();
	    return sameModel;	
	}
	
	public void persistClustering(String path, char separator) throws IOException
	{
	    FileWriter fw = new FileWriter(path,true);
	    BufferedWriter bw = new BufferedWriter(fw,250000000);
	    for (PointOfSale pos : pointsOfSale)
	    {
		bw.write(Integer.toString(pos.getCode())+separator);
	    }
	    bw.newLine();
	    JavaRDD<Vector> data = this.getJavaRDD(separator);
	    JavaRDD<Integer> parsedData= this.getkMeansModel().predict(data);
	    List<Integer> liste =  parsedData.collect();
	    for (Integer i : liste)
	    {
		bw.write(i.toString()+separator);
	    }
	    bw.close();
	}
	
	public void predictAll(String mapFile, char separator) throws IOException
	{
		CSVUsage csv = new CSVUsage(mapFile,separator);
		String [] POSCodes = csv.getCsvreader().readNext();
		String [] POSClusters = csv.getCsvreader().readNext();
		for (int i=0;i<POSCodes.length-1;i++)
		{
			this.getClusterMapping().put(Integer.parseInt(POSCodes[i]), Integer.parseInt(POSClusters[i]));
		}
		for (PointOfSale elem : pointsOfSale )
		{
			elem.setClusterIndex(this.getClusterMapping().get(elem.getCode()));
		}
	}
	public List<Vector> toVec()
	{
	    List<Vector> output = new ArrayList<Vector>();
	    SparkSession spark = SparkSession
			.builder()
			.appName("JavaCountVectorizerExample")
			.master("local[*]")
			.getOrCreate();
	    List<Row> data= new ArrayList<Row>();
	    for (PointOfSale elem : this.getPointsOfSale())
	    {
		data.add( RowFactory.create(elem.getServices()));
	    }
	    StructType schema = new StructType(new StructField [] {
		    new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
	    });
	    Dataset<Row> df = spark.createDataFrame(data, schema);
	    CountVectorizerModel cvm = new CountVectorizerModel(new String[]{"Annonce","BuyBox","CDiscount","CanalPlus","Cash Way","ChaabiDirect","Commande Livraison Tabac","Coyote","Digitick","Hotline","Librairie","Moneygram",
        				"MoyensPaiement","Orange Money","Presse","SpiritofCadeau","Telephonie","TimbreAmende","TransferTo","TransferVideo",
        				"Vente de produits physiques","Vente produits Compte Nickel","ViaPresse","WonderBox","Yes by Cash"})
        	      .setInputCol("text")
        	      .setOutputCol("feature");
	    int index =0;
	    for (Row row : cvm.transform(df).collectAsList() )
	    {
		double[] values=((SparseVector) (row.get(1))).toDense().values();
		double[] values2 = new double[28];
        	PointOfSale current = this.getPointsOfSale().get(index);
        	values2[0]=current.getNbChekout();
        	values2[1]=current.getLatitude();
        	values2[2]=current.getLongitude();
        	for (int i = 3; i < 28; i++) {
        	    values2[i] = values[i-3];
            	}
        	    current.setServicesBin(values);
        	    Vector vector =Vectors.dense(values2);
        	    output.add(vector);
        	    index++;
        	}
        	spark.close();
	    return output;
	}
}
