package fr.ensma.lias.bimedia2018machinelearning.preprocessing;

import java.io.IOException;
import java.util.List;

import org.apache.spark.mllib.linalg.Vector;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.POSControl;

public class ClusteringMain {
    public static void main( String[] args ) 
    {
	POSControl control = new POSControl();
	String folder=System.getProperty("user.dir");
	folder+="/src/main/resources/";
	try {
	    control.createPointsOfSale(folder+"/Stores/StoresBimedia.csv", ';', true, false);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	List<Vector> vectors = control.toVec();
	for(Vector elem : vectors)
	{
	    System.out.println(elem);
	}
	control.ClusKMeans(2, ';');
	control.setkMeansModel(control.loadModel("StoresBimedia", "local[*]", "target/KMeansModel"));
	try {
	    control.persistClustering(folder+"References/storesClustered.csv", ';');
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
