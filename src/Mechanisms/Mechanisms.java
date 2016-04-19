package Mechanisms;

import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Motivation.Motivation;
import MentalState.MentalState;
import MetaInformation.Turns;

public class Mechanisms {

	public enum AGENT{SELF, OTHER, BOTH, UNKNOWN};

	public MentalState mentalState;
	
	protected Turns currentTurn;
	protected Collaboration collaboration;
	protected Motivation motivation;
	
//	public Mechanisms() {}
	
//	public Mechanisms(MentalState mentalState) {
//		turn = new Turns();
//		this.mentalState = mentalState;
//	}
}