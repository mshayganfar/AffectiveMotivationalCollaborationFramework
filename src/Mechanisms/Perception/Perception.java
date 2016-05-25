package Mechanisms.Perception;

import Mechanisms.Mechanisms.AGENT;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import MentalState.Goal;
import MetaInformation.AppraisalVector.EMOTION_INSTANCE;
import MetaInformation.MentalProcesses;
import edu.wpi.cetask.Plan;

public class Perception{
	
	public enum EVENT_CATEGORIES {OTHER_ACHIEVED, SELF_ACHIEVED, BOTH_ACHIEVED, OTHER_FAILED, SELF_FAILED, BOTH_FAILED, OTHER_PROPOSED, SELF_PROPOSED, OTHER_ACCEPTED, SELF_ACCEPTED, OTHER_REJECTED, SELF_REJECTED};
	
	private double emotionValence;
	private Collaboration collaboration;
	
	public Perception() {
		emotionValence = 0.0;
	}
	
	public void preparePerceptionMechanism(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
	}
	
//	public Perception getInstance() {
//		return perception;
//	}
	
	public double perceiveEmotion(EMOTION_INSTANCE emotion, double intensity) {

		return (emotion.equals(EMOTION_INSTANCE.JOY)) ? intensity : (emotion.equals(EMOTION_INSTANCE.NEUTRAL)) ? 0 : (-1)*intensity;
	}
	
	public double getEmotionValence() { 
		return emotionValence;
	}
	
	public void setEmotionValence(double valenceValue) { 
		
		this.emotionValence = valenceValue;
	}
	
	public EVENT_CATEGORIES getEventCategory(Goal eventGoal) {
		
		Plan plan = eventGoal.getPlan();
		
		AGENT responsibleAgent = collaboration.getResponsibleAgent(plan);
		
		if (responsibleAgent.equals(AGENT.SELF)) {
			if (collaboration.getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED))
				return EVENT_CATEGORIES.SELF_ACHIEVED;
			else if (collaboration.getGoalStatus(plan).equals(GOAL_STATUS.FAILED))
				return EVENT_CATEGORIES.SELF_FAILED;
		}
		else if (responsibleAgent.equals(AGENT.OTHER)) {
			if (collaboration.getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED))
				return EVENT_CATEGORIES.OTHER_ACHIEVED;
			else if (collaboration.getGoalStatus(plan).equals(GOAL_STATUS.FAILED))
				return EVENT_CATEGORIES.OTHER_FAILED;
		}
		else if (responsibleAgent.equals(AGENT.BOTH)) {
			if (collaboration.getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED))
				return EVENT_CATEGORIES.BOTH_ACHIEVED;
			else if (collaboration.getGoalStatus(plan).equals(GOAL_STATUS.FAILED))
				return EVENT_CATEGORIES.BOTH_FAILED;
		}
		else
			throw new IllegalArgumentException("Illegal Agent Type: " + responsibleAgent);
		
		return null;
	}
}