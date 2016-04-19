package Mechanisms.ToM;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.Appraisal.Relevance;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Motivation.Motivation;

public class ToM extends Mechanisms{
	
	private Collaboration collaboration;
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	private Motivation motivation;
	
	private double valence = 0.0;
	
	public double getValenceValue() {
		return valence;
	}
	
	public void prepareToM(Collaboration collaboration, Motivation motivation, Relevance relevance, Controllability controllability, Desirability desirability, Expectedness expectedness) {
		this.collaboration = collaboration;
		this.motivation    = new Motivation();
		
		this.relevance       = new Relevance();
		this.controllability = new Controllability();
		this.desirability    = new Desirability();
		this.expectedness    = new Expectedness();
	}
}