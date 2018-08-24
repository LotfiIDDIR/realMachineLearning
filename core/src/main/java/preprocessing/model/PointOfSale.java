package fr.ensma.lias.bimedia2018machinelearning.preprocessing.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.SparseVector;
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

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.fileReadWrite.ICSV;

/**
 * @author  Lotfi IDDIR
 */

public class PointOfSale implements ICSV{
	
	private int code;
	private String address;
	private int nbChekout;
	private List<String> services;
	private double[]servicesBin;
	private int codePostal;
	private String ville;
	private int clusterIndex;
	private double longitude;
	private double latitude;
	
	//Getters and Setters
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getNbChekout() {
		return nbChekout;
	}
	public void setNbChekout(int nbChekout) {
		this.nbChekout = nbChekout;
	}
	public List<String> getServices() {
		return services;
	}
	public void setServices(List<String> services) {
		this.services = services;
	}
	public double[] getServicesBin() {
		return servicesBin;
	}
	public void setServicesBin(double[] servicesBin) {
		this.servicesBin = servicesBin;
	}
	public int getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(int codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public int getClusterIndex() {
		return clusterIndex;
	}
	public void setClusterIndex(int clusterIndex) {
		this.clusterIndex = clusterIndex;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	// Constructors
	public PointOfSale() {
		super();
		this.services=new ArrayList<String>();
		this.servicesBin = new double[25];
	}

	@Override
	public ICSV readCSV(String[] line, boolean treated) {
		this.setCode(Integer.parseInt(line[2]));
		this.setAddress(line[5]);
		this.setCodePostal(Integer.parseInt(line[6]));
		this.setVille(line[7]);
		this.setLatitude(Double.parseDouble(line[9]));
		this.setLongitude(Double.parseDouble(line[10]));
		this.setNbChekout(Integer.parseInt(line[11]));	
		if(treated)
		{	
		    for(int i=0;i<25;i++)
		    {
			this.getServicesBin()[i]=Integer.parseInt(line[i+11]);
		    }
		}
		else
		{
		    String [] services = line[12].split(",");
		    for(int i=0;i<services.length-1;i++)
		    {
			this.getServices().add(services[i]);
		    }	
		}
		return this;
	}
	@Override
	public String writeCSV(char separator) {
		String result ="";
		result+= this.getCode();
		result+=separator;
		result+= this.getAddress();
		result+=separator;
		result+= this.getCodePostal();
		result+=separator;
		result+= this.getVille();
		result+=separator;
		result+= this.getNbChekout();
		result+=separator;
		result+=this.getServicesBinString(separator);
		return result;
	}
	
	public String getServicesBinString(char separator) {
		String result = "";
		int i=0;
		for (i=0;i<this.getServicesBin().length-1;i++) {
			result+=((int)this.getServicesBin()[i]);
			result+=separator;
		}
		result+=((int)this.getServicesBin()[i]);
		return result;
	}
	public String getPOSToClustering(char separator)// Ajouter le snouveaux attributs
	{
		String elemString = "";
		elemString+=this.getNbChekout();
		elemString+=separator;
		elemString+=this.getLongitude();
		elemString+=separator;
		elemString+=this.getLatitude();
		elemString+=separator;
		elemString+=this.getServicesBinString(separator);
		return elemString;
		
	}
	
	public Vector toVec()// A changer lors de l'ajout de l'adresse
	{
		SparkSession spark = SparkSession
				.builder()
				.appName("JavaCountVectorizerExample")
				.master("local[*]")
				.getOrCreate();
		List<Row> data= new ArrayList<Row>();
		
		data.add( RowFactory.create(this.getServices()));
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
		double[] values=((SparseVector) (row.get(1))).toDense().values();
		double[] values2 = new double[28];
		values[0]=this.getNbChekout();
		values[1]=this.getLatitude();
		values[2]=this.getLongitude();
		for (int i = 3; i < 28; i++) {
			values2[i] = values[i-3];
		}
		Vector vector =Vectors.dense(values);
		spark.close();
		return vector;
	}
	
	public Vector toVec(SparkSession spark)// A changer lors de l'ajout de l'adresse
	{
		List<Row> data= new ArrayList<Row>();
		
		data.add( RowFactory.create(this.getServices()));
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
		double[] values=((SparseVector) (row.get(1))).toDense().values();
		double[] values2 = new double[28];
		values[0]=this.getNbChekout();
		values[1]=this.getLatitude();
		values[2]=this.getLongitude();
		for (int i = 3; i < 28; i++) {
			values2[i] = values[i-3];
		}
		Vector vector =Vectors.dense(values);
		return vector;
	}
	public int predict(KMeansModel model)
	{
		return model.predict(this.toVec());
	}
	
	public void setServicesBinFromString()
	{
		SparkSession spark = SparkSession
				.builder()
				.appName("JavaCountVectorizerExample")
				.master("local[*]")
				.getOrCreate();
		List<Row> data= new ArrayList<Row>();
		data.add(RowFactory.create(this.getServices()));
		StructType schema = new StructType(new StructField [] {
			      new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
			    });
		Dataset<Row> df = spark.createDataFrame(data, schema);
		CountVectorizerModel cvm = new CountVectorizerModel(new String[]{"Annonce","BuyBox","CDiscount","CanalPlus","Cash Way","ChaabiDirect","Commande Livraison Tabac","Coyote","Digitick","Hotline","Librairie","Moneygram",
				"MoyensPaiement","Orange Money","Presse","SpiritofCadeau","Telephonie","TimbreAmende","TransferTo","TransferVideo",
				"Vente de produits physiques","Vente produits Compte Nickel","ViaPresse","WonderBox","Yes by Cash"})
				.setInputCol("text")
			    .setOutputCol("feature");

		List<Row> liste= cvm.transform(df).collectAsList();
		for(Row elem : liste)
		   {
			   SparseVector vector = (SparseVector) elem.get(1);
			   DenseVector vector2= vector.toDense();
			   for(int i=0;i<25;i++)
			   {
				   servicesBin[i]=(int)vector2.values()[i];
			   }
		   }
	}
	
	public String toString()
	{
	    String result ="Code : ";
		result+= this.getCode();
		result+="\n Adresse : ";
		result+= this.getAddress();
		result+="\n Code postal : ";
		result+= this.getCodePostal();
		result+="\n Ville : ";
		result+= this.getVille();
		result+="\n Nombre de caisses : ";
		result+= this.getNbChekout();
		result+="\n Services : ";
		result+=this.getServices();
		result+=" \n Latitude :";
		result+= this.getLatitude();
		result+="\n Longitude :";
		result+= this.getLongitude();
		return result;
	}
}
