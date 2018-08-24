package fr.ensma.lias.bimedia2018machinelearning.preprocessing.model;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AssociationRule {
	
	private List<String>antecedent;
	private List<String>consequent;
	private double confidence;
	
	// Getters and Setters
	public List<String> getAntecedent() {
		return antecedent;
	}
	public void setAntecedent(List<String> antecedent) {
		this.antecedent = antecedent;
	}
	public List<String> getConsequent() {
		return consequent;
	}
	public void setConsequent(List<String> consequent) {
		this.consequent = consequent;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confience) {
		this.confidence = confience;
	}
	
	//Constructors
	public AssociationRule(List<String> antecedent, List<String> consequent, double confience) {
		super();
		this.antecedent = antecedent;
		this.consequent = consequent;
		this.confidence = confience;
	}
	public AssociationRule() {
		super();
	}
	
	public String writeJson() {
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
		 String strJson = gson.toJson(this);
		return strJson;
	}
	public void readJson(String json) {
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
		 AssociationRule assoc = gson.fromJson(json, AssociationRule.class);
		System.out.println(assoc.getAntecedent());
	}
}
