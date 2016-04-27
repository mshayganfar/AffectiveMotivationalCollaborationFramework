package MetaInformation;

import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Coping;
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
	private Coping coping;
	
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	
	public MentalProcesses(String[] args) {
		this.collaboration = new Collaboration(args);
		this.motivation    = new Motivation();
		this.tom           = new ToM();
		this.coping		   = new Coping();
		
		this.relevance       = new Relevance(this);
		this.controllability = new Controllability(this);
		this.desirability    = new Desirability(this);
		this.expectedness    = new Expectedness(this);
		
		this.motivation.prepareMotivationMechanism(this);
		this.tom.prepareAppraisalsOfToM(this);
		this.coping.prepareAppraisalsOfToM(this);
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
	
	public Coping getCopingMechanism() {
		return this.coping;
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
