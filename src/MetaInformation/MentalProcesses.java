package MetaInformation;

import Mechanisms.Action.Action;
import Mechanisms.Action.DiscoActionsWrapper;
import Mechanisms.Appraisal.Appraisal;
import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Coping;
import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.Appraisal.Relevance;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Motivation.Motivation;
import Mechanisms.Perception.Perception;
import Mechanisms.ToM.ToM;

public class MentalProcesses {
	private Perception perception;
	private Collaboration collaboration;
	private Motivation motivation;
	private ToM tom;
	private Appraisal appraisal;
	private Coping coping;
	private Action action;
	
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	
	public MentalProcesses(String[] args, boolean AMCrun) {
		
		this.collaboration = new Collaboration(args, AMCrun);
		this.appraisal	   = new Appraisal();
		this.perception    = new Perception();
		this.motivation    = new Motivation();
		this.tom           = new ToM();
		this.coping		   = new Coping();
		this.action        = new Action(new DiscoActionsWrapper(this));
		
		this.relevance       = new Relevance(this);
		this.controllability = new Controllability(this);
		this.desirability    = new Desirability(this);
		this.expectedness    = new Expectedness(this);
		
		this.collaboration.prepareCollaborationMechanism(this);
		this.perception.preparePerceptionMechanism(this);
		this.motivation.prepareMotivationMechanism(this);
		this.tom.prepareAppraisalsOfToM(this);
		this.coping.prepareCopingMechanism(this);
		this.action.prepareActionMechanism(this);
		if (AMCrun) {
			this.collaboration.getAMCAgent().prepareAgent(this);
			this.collaboration.getAMCUser().prepareUser(this);
		}
		else {
			this.collaboration.getDiscoAgent().prepareAgent(this);
			this.collaboration.getDiscoUser().prepareUser(this);
		}
		this.appraisal.prepareAppraisal(this);
	}
	
	public Perception getPerceptionMechanism() {
		return this.perception;
	}
	
	public Action getActionMechanism() {
		return this.action;
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
	
	public Appraisal getAppraisalProcess() {
		return this.appraisal;
	}
}
