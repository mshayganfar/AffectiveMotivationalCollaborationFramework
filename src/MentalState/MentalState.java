package MentalState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import MetaInformation.Turns;
import edu.wpi.cetask.Plan;

public class MentalState {
	
	private static MentalState mentalStates = new MentalState();
	
	private static Turns currentTurn;
	
	private static ArrayList<Goal> goals     = new ArrayList<Goal>();
	private static ArrayList<Belief> beliefs = new ArrayList<Belief>();
	private static ArrayList<Motive> motives = new ArrayList<Motive>();
	
	private static Map goalBeliefs = new HashMap();
	
	private MentalState() { }
	
	public static void updateMentalStateBeliefs(Goal goal, Belief belief) {
		beliefs.add(belief);
		goalBeliefs.put(goal, beliefs);
	}
	
	public static ArrayList<Belief> getBeliefs (Goal goal) {
		
		return (ArrayList<Belief>)goalBeliefs.get(goal);
	}
	
	public static ArrayList<Goal> getGoals () {
		
		return goals;
	}
	
	public static MentalState getInstance() {
		return mentalStates;
	}
	
	public static void addGoal(Goal goal) {
		goals.add(goal);
	}
	
	public static void addBelief(Belief belief) {
		beliefs.add(belief);
	}
	
	public static void addMotive(Motive motive) {
		motives.add(motive);
	}
	
	public static Goal getParentGoal(Goal childGoal) {
		for (Goal goal : goals) {
			if (childGoal.getParentPlan().getType().equals(goal.getPlan().getType()))
				return goal;
		}
		return null;
	}
	
	public static Goal getSpecificGoal(Plan specificPlan) {
		
		for (Goal goal : goals) {
			if (goal.getPlan().getType().equals(specificPlan.getType()))
				return goal;
		}
		
		return null;
	}
	
	public static boolean isGoalInMentalState(Plan plan) {
		
		for (Goal goal : goals)
//			if (goal.getPlan().getGoal().getType().equals(plan.getGoal().getType()))
			if (goal.getPlan().getGoal().equals(plan.getGoal()))
				return true;
		return false;
	}
	
//	public static void updateTurn(Turns turn) {
//		this.currentTurn = turn;
//	}
}