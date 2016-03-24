package Mechanisms.Appraisal;

import Mechanisms.Mechanisms;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Perception.Perception;

public class AppraisalProcesses extends Mechanisms {

//	protected Collaboration collaboration;
	
	public AppraisalProcesses() {
//		this.perception = new Perception();
//		this.collaboration = collaboration;
//		System.out.println("Constructor: appraisal works!");
	}
	
	// The return value can be computed according to the emotional status of the human.
	protected double getHumanEmotionalThreshold() { return 0.0; }
	
	public Collaboration getCollaboration() { return this.collaboration; }
}
