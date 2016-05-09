package Mechanisms.Action;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Coping;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MentalState.Intention;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.CopingElicitationFrame.COPING_STRATEGY;
import edu.wpi.disco.Disco;
import edu.wpi.disco.lang.Say;

public class Action extends Mechanisms{

	private Coping coping;
	private ToM tom;
	
	private DiscoActionsWrapper discoActionsWrapper;
	
	public Action() {
		discoActionsWrapper  = new DiscoActionsWrapper(collaboration);
	}
	
	public void prepareCopingMechanism(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.coping 	   = mentalProcesses.getCopingMechanism();
		this.tom    	   = mentalProcesses.getToMMechanism();
	}
	
	public void act(Goal goal) {
		
		System.out.println("Agent chose the following coping strtegies in turn: " + Turns.getInstance().getTurnNumber());
		
		for (Intention intention : goal.getIntentions()) {
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS))
				coping.doSeekingSocialSupportForInstrumentalReasons();
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.ACTIVE_COPING))
				coping.doActiveCoping(goal);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.PLANNING))
				coping.doPlanning(goal, false);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.WISHFUL_THINKING))
				coping.doWishfulThinking();
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.MENTAL_DISENGAGEMENT))
				coping.doMentalDisengagement();
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.SHIFTING_RESPONSIBILITY))
				coping.doShiftingResponsibility();
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.ACCEPTANCE))
				coping.doAcceptance(goal, false);
		}
	}
	
	public void acknowledgeEmotion(Goal eventGoal) {
		
		switch(tom.getAnticipatedHumanEmotion(eventGoal)) {
			case ANGER:
				new Say.Agent(collaboration.getDisco(), "I see you are angry!");
				break;
			case FRUSTRATION:
				new Say.Agent(collaboration.getDisco(), "I see! It is frustrating!");
				break;
			case SADNESS:
				new Say.Agent(collaboration.getDisco(), "It is so sad!");
				break;
			case WORRY:
				new Say.Agent(collaboration.getDisco(), "Don't worry!");
				break;
			case NEGATIVE_SURPRISE:
				new Say.Agent(collaboration.getDisco(), "I was not expecting that either!");
				break;
			case GUILT:
				new Say.Agent(collaboration.getDisco(), "It was not your fault!");
				break;
		}
	}
	
	public void expressEmotion(Goal eventGoal) {
		
		switch(Turns.getInstance().getAppraisalVector(eventGoal).getEmotionInstance()) {
			case ANGER:
				new Say.Agent(collaboration.getDisco(), "I am Angry!");
				break;
			case FRUSTRATION:
				new Say.Agent(collaboration.getDisco(), "I am frustrated!");
				break;
			case SADNESS:
				new Say.Agent(collaboration.getDisco(), "I am sad!");
				break;
			case WORRY:
				new Say.Agent(collaboration.getDisco(), "I am worried!");
				break;
			case NEGATIVE_SURPRISE:
				new Say.Agent(collaboration.getDisco(), "Oh no! I am surprised");
				break;
			case GUILT:
				new Say.Agent(collaboration.getDisco(), "Sorry, it is my fault!");
				break;
			case JOY:
				new Say.Agent(collaboration.getDisco(), "I am happy!");
				break;
			case POSTIVE_SURPRISE:
				new Say.Agent(collaboration.getDisco(), "Wow, I was not expecting that!");
				break;
			case GRATITUDE:
				new Say.Agent(collaboration.getDisco(), "Thank you for doing this!");
				break;
			case NEUTRAL:
				new Say.Agent(collaboration.getDisco(), "No feeling!");
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
