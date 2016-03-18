package MentalState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Mechanisms.Collaboration.Collaboration;
import MetaInformation.Turns;

public class MentalState {
	
	private Turns currentTurn;
	
	private ArrayList<Goal> goals     = new ArrayList<Goal>();
	private ArrayList<Belief> beliefs = new ArrayList<Belief>();
	
	private Map goalBeliefs = new HashMap();
	
	public void updateMentalState(Goal goal, Belief belief) {
		beliefs.add(belief);
		goalBeliefs.put(goal, beliefs);
	}
	
	public ArrayList<Belief> getBeliefs (Goal goal) {
		
		return (ArrayList<Belief>)goalBeliefs.get(goal);
	}
	
	public MentalState(Collaboration collaboraiton) {
//		this.currentTurn = collaboraiton.getCurrentTurn();
	}
	
	public void addGoal(Goal goal) {
		goals.add(goal);
	}
	
	public void addBelief(Goal goal, Belief belief) {
		beliefs.add(belief);
	}
	
	public void updateTurn(Turns turn) {
		this.currentTurn = turn;
	}
}