package Mechanisms;

import Mechanisms.Appraisal.Desirability;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Motivation.Motivation;
import Mechanisms.Perception.Perception;
import MentalState.MentalState;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;

public class Mechanisms {

	public enum AGENT{SELF, OTHER, BOTH, UNKNOWN};

	public MentalState mentalState;
	
	protected MentalProcesses mentalProcesses;
	
	protected Perception perception;
	protected Collaboration collaboration;
	protected Motivation motivation;
	
	protected Desirability desirability;
	
	protected Turns currentTurn;
	
//	public Mechanisms() {}
	
//	public Mechanisms(MentalState mentalState) {
//		turn = new Turns();
//		this.mentalState = mentalState;
//	}
}