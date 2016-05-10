package Mechanisms.Action;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Coping;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MentalState.Intention;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.CopingActivation.COPING_STRATEGY;

public class Action extends Mechanisms{

	private Coping coping;
	private ToM tom;
	
	private DiscoActionsWrapper discoActionsWrapper;
	
	public Action() {
		discoActionsWrapper  = new DiscoActionsWrapper(collaboration);
	}
	
	public void prepareActionMechanism(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.coping 	   = mentalProcesses.getCopingMechanism();
		this.tom    	   = mentalProcesses.getToMMechanism();
	}
	
	public void act(Goal goal) {
		
		System.out.println("Agent chose the following coping strtegies in turn: " + Turns.getInstance().getTurnNumber());
		
		for (Intention intention : goal.getIntentions()) {
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS))
				coping.doSeekingSocialSupportForInstrumentalReasons(goal);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.ACTIVE_COPING))
				coping.doActiveCoping(goal);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.PLANNING))
				coping.doPlanning(goal, false);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.WISHFUL_THINKING))
				coping.doWishfulThinking(goal);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.MENTAL_DISENGAGEMENT))
				coping.doMentalDisengagement(goal);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.SHIFTING_RESPONSIBILITY))
				coping.doShiftingResponsibility(goal);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.ACCEPTANCE))
				coping.doAcceptance(goal, false);
		}
	}
	
	public void acknowledgeEmotion(Goal eventGoal) {
		
		switch(tom.getAnticipatedHumanEmotion(eventGoal)) {
			case ANGER:
				discoActionsWrapper.saySomethingAboutTask(false, "I see you are angry!");
				break;
			case FRUSTRATION:
				discoActionsWrapper.saySomethingAboutTask(false, "I see! It is frustrating!");
				break;
			case SADNESS:
				discoActionsWrapper.saySomethingAboutTask(false, "It is so sad!");
				break;
			case WORRY:
				discoActionsWrapper.saySomethingAboutTask(false, "Don't worry!");
				break;
			case NEGATIVE_SURPRISE:
				discoActionsWrapper.saySomethingAboutTask(false, "I was not expecting that!");
				break;
			case GUILT:
				discoActionsWrapper.saySomethingAboutTask(false, "It was not your fault!");
				break;
		}
	}
	
	public void expressEmotion(Goal eventGoal) {
		
		switch(Turns.getInstance().getAppraisalVector(eventGoal).getEmotionInstance()) {
			case ANGER:
				discoActionsWrapper.saySomethingAboutTask(false, "I am Angry!");
				break;
			case FRUSTRATION:
				discoActionsWrapper.saySomethingAboutTask(false, "I am frustrated!");
				break;
			case SADNESS:
				discoActionsWrapper.saySomethingAboutTask(false, "I am sad!");
				break;
			case WORRY:
				discoActionsWrapper.saySomethingAboutTask(false, "I am worried!");
				break;
			case NEGATIVE_SURPRISE:
				discoActionsWrapper.saySomethingAboutTask(false, "Oh no! I am surprised");
				break;
			case GUILT:
				discoActionsWrapper.saySomethingAboutTask(false, "Sorry, it is my fault!");
				break;
			case JOY:
				discoActionsWrapper.saySomethingAboutTask(false, "I am happy!");
				break;
			case POSTIVE_SURPRISE:
				discoActionsWrapper.saySomethingAboutTask(false, "Wow, I was not expecting that!");
				break;
			case GRATITUDE:
				discoActionsWrapper.saySomethingAboutTask(false, "Thank you for doing this!");
				break;
			case NEUTRAL:
				discoActionsWrapper.saySomethingAboutTask(false, "No feeling!");
				break;
		}
	}
	
	public DiscoActionsWrapper getDiscoActions() {
		return this.discoActionsWrapper;
	}
	
	public void executeDomainAction(DomainAction action) {
		action.run();
	}
	
	public void say(String utterance) {
		System.out.println(utterance);
	}
	
	public void ask(String utterance) {
		System.out.println(utterance);
	}
}
