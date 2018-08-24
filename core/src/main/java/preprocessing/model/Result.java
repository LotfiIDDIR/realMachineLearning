package fr.ensma.lias.bimedia2018machinelearning.preprocessing.model;

import java.util.ArrayList;
import java.util.List;

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
	
}
