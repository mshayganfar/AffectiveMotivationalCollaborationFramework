package Mechanisms.Appraisal;

import Mechanisms.Collaboration.Collaboration;
import MentalState.Goal;
import edu.wpi.cetask.DecompositionClass;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.Task;
import edu.wpi.disco.lang.Accept;
import edu.wpi.disco.lang.Ask;
import edu.wpi.disco.lang.Mention;
import edu.wpi.disco.lang.Propose;
import edu.wpi.disco.lang.Reject;
import edu.wpi.disco.lang.Utterance;

public class DiscoActionsWrapper {
	
	private Collaboration collaboration;
	
	public DiscoActionsWrapper(Collaboration collaboration) {
		this.collaboration = collaboration;
	}
	
	void proposeTaskWho(Goal goal, boolean speaker) {
		
		// NOTE: Speaker should be false when the agent is doing coping.
		
		Plan plan = goal.getPlan();
		
		if (plan.isPrimitive()) {
			Utterance taskToPropose = new Propose.Who(collaboration.getDisco(), speaker, plan.getGoal(), !speaker);
			collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);
		}
		else
			throw new IllegalArgumentException("Both of the collaborators are responsible for non-primitives");
	}
	
	void proposeTaskShould(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToPropose = Propose.Should.newInstance(collaboration.getDisco(), speaker, plan.getGoal());
		collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);
	}

	void proposeTaskWhat(Goal goal, boolean speaker, String slot, Object value) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToPropose = new Propose.What(collaboration.getDisco(), speaker, plan.getGoal(), slot, value);
		collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);
	}
	
	void proposeTaskHow(Goal goal, boolean speaker, DecompositionClass decomp) {
		
		Plan plan = goal.getPlan();
		
		if (!plan.isPrimitive()) {
			Utterance taskToPropose = new Propose.How(collaboration.getDisco(), speaker, plan, decomp);
			collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);
		}
		else
			throw new IllegalArgumentException("Primitives does not need recepies!");
	}
	
	void acceptProposedTask(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		Utterance acceptance = new Accept(collaboration.getDisco(), speaker, Propose.Should.newInstance(collaboration.getDisco(), speaker, plan.getGoal()));
		collaboration.getDisco().getInteraction().occurred(speaker, acceptance, null);
	}
	
	void rejectProposedTask(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		Utterance rejection = new Reject(collaboration.getDisco(), speaker, Propose.Should.newInstance(collaboration.getDisco(), speaker, plan.getGoal()));
		collaboration.getDisco().getInteraction().occurred(speaker, rejection, null);
	}
	
	void mentionTask(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToMention = new Mention(collaboration.getDisco(), speaker, plan.getGoal());
		collaboration.getDisco().getInteraction().occurred(speaker, taskToMention, plan);
	}
	
	void askAboutTaskWho(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		if (plan.isPrimitive()) {
			Utterance taskToAsk = new Ask.Who(collaboration.getDisco(), speaker, plan.getGoal());
			collaboration.getDisco().getInteraction().occurred(speaker, taskToAsk, plan);
		}
		else
			throw new IllegalArgumentException("Both of the collaborators are responsible for non-primitives");
	}
	
	void askAboutTaskShould(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToAsk = new Ask.Should(collaboration.getDisco(), speaker, plan.getGoal());
		collaboration.getDisco().getInteraction().occurred(speaker, taskToAsk, plan);
	}
	
	void askAboutTaskWhat(Goal goal, boolean speaker, String slot) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToAsk = new Ask.What(collaboration.getDisco(), speaker, plan.getGoal(), slot);
		collaboration.getDisco().getInteraction().occurred(speaker, taskToAsk, plan);
	}
	
	void askAboutTaskHow(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		if (!plan.isPrimitive()) {
			Utterance taskToAsk = new Ask.How(collaboration.getDisco(), speaker, plan.getGoal());
			collaboration.getDisco().getInteraction().occurred(speaker, taskToAsk, plan);
		}
		else
			throw new IllegalArgumentException("Primitives does not need recepies!");
	}

	void executeTask(Goal goal, boolean actor) {
		
		Plan plan = goal.getPlan();
		
		if (plan.isPrimitive())
			collaboration.getDisco().getInteraction().occurred(actor, plan.getGoal(), plan);
		else
			throw new IllegalArgumentException("Non-primitive tasks can not be executed!");
	}
}
