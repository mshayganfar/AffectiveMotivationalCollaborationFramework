package Mechanisms.Appraisal;

import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Perception.Perception;

public class AppraisalProcesses{

	protected Perception perception;
	protected Collaboration collaboration;
	
	public AppraisalProcesses(String[] args) {
//		this.perception = new Perception();
//		this.collaboration = new Collaboration(args);
		System.out.println("Constructore: appraisal works!");
	}
	
	// The return value can be computed according to the emotional status of the human.
	protected double getHumanEmotionalThreshold() { return 0.0; }
	
	public static void testAppraisal() { System.out.println("appraisal works!"); }
}
