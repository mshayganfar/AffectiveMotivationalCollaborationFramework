package MetaInformation;

import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.Appraisal.Relevance;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Motivation.Motivation;
import Mechanisms.ToM.ToM;

public class MentalProcesses {
	private Collaboration collaboration;
	private Motivation motivation;
	private ToM tom;
	
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	
	public MentalProcesses(String[] args) {
		this.collaboration = new Collaboration(args);
		this.motivation    = new Motivation();
		this.tom           = new ToM();
		
		this.relevance       = new Relevance();
		this.controllability = new Controllability();
		this.desirability    = new Desirability();
		this.expectedness    = new Expectedness();
		
		relevance.prepareRelevance(collaboration, motivation);
		controllability.prepareControllability(collaboration);
		desirability.prepareDesirability(collaboration);
		expectedness.prepareExpectedness(collaboration);
		
		motivation.prepareMotivation(tom, controllability, expectedness);
		tom.prepareToM(collaboration, motivation, relevance, controllability, desirability, expectedness);
	}
	
	public Collaboration getCollaborationMechanism() {
		return this.collaboration;
	}
	
	public Motivation getMotivationMechanism() {
		return this.motivation;
	}
	
	public ToM getToMMechanism() {
		return this.tom;
	}
	
	public Relevance getRelevanceProcess() {
		return this.relevance;
	}
	
	public Desirability getDesirabilityProcess() {
		return this.desirability;
	}
	
	public Controllability getControllabilityProcess() {
		return this.controllability;
	}
	
	public Expectedness getExpectednessProcess() {
		return this.expectedness;
	}
}
