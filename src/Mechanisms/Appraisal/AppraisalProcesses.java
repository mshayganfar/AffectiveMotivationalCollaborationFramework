package Mechanisms.Appraisal;

import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Perception.Perception;

public class AppraisalProcesses{

	protected Perception perception;
	protected Collaboration collaboration;
	
	public AppraisalProcesses(String[] args) {
		this.perception = new Perception();
		this.collaboration = new Collaboration(args);
	}
	
	// The return value can be computed according to the emotional status of the human.
	protected double getHumanEmotionalThreshold() { return 0.0; }
}
