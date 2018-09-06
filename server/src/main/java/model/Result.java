package fr.ensma.lias.bimedia2018machinelearning.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Result {
	
	private List<AssociationRule> rules;

	public List<AssociationRule> getRules() {
		return rules;
	}

	public void setRules(List<AssociationRule> rules) {
		this.rules = rules;
	}

	public Result() {
		super();
		this.rules= new ArrayList<AssociationRule>();
	}
	
	public String writeJson() {
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
		 String strJson = gson.toJson(this);
		return strJson;
	}
	
	public Result readJson(String path) {
		Gson gson = new Gson();
		BufferedReader br = null;
		Result result = null;
	    try {
	        br = new BufferedReader(new FileReader(path));
	        result = gson.fromJson(br, Result.class);
	        for (AssociationRule rule : result.getRules())
	        	System.out.println(rule.writeJson());
	     } catch (FileNotFoundException e) {
	         e.printStackTrace();
	       } finally {
	         if (br != null) {
	          try {
	           br.close();
	          } catch (IOException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	          }
	         }
	       }
	    return result;
	}
	
}
