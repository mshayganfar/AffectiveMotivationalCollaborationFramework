package Mechanisms.Appraisal;

import Mechanisms.Collaboration.Collaboration;
import MentalState.Goal;
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
	
	void proposeTask(Goal goal, Plan contributes, boolean speaker) {
		
		// NOTE: Speaker should be false when the agent is doing coping.
		
		Task taskToPropose;
		Task task = goal.getPlan().getGoal();
		
		if (task.isPrimitive())
			taskToPropose = new Propose.Who(collaboration.getDisco(), speaker, task, !speaker);
		else
			taskToPropose = new Propose.Should(collaboration.getDisco(), speaker, task);
		
		collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, contributes);
	}
	
	void acceptProposedTask(Goal goal, Plan contributes, boolean speaker) {
		
		Task task = goal.getPlan().getGoal();
		
		Utterance acceptance = new Accept(collaboration.getDisco(), speaker, new Propose.Should(collaboration.getDisco(), speaker, task));
		collaboration.getDisco().getInteraction().occurred(speaker, acceptance, contributes);
	}
	
	void rejectProposedTask(Goal goal, Plan contributes, boolean speaker) {
		
		Task task = goal.getPlan().getGoal();
		
		Utterance rejection = new Reject(collaboration.getDisco(), speaker, new Propose.Should(collaboration.getDisco(), speaker, task));
		collaboration.getDisco().getInteraction().occurred(speaker, rejection, contributes);
	}
	
	void mentionTask(Goal goal, Plan contributes, boolean speaker) {
		
		Task taskToMention = new Mention(collaboration.getDisco(), speaker, goal.getPlan().getGoal());
		collaboration.getDisco().getInteraction().occurred(speaker, taskToMention, contributes);
	}
	
	void askAboutTask(Goal goal, Plan contributes, boolean speaker) {
		
		Task taskToAsk = new Ask.Who(collaboration.getDisco(), speaker, goal.getPlan().getGoal());
		collaboration.getDisco().getInteraction().occurred(speaker, taskToAsk, contributes);
	}
	
	void executeTask(Goal goal, Plan contributes, boolean speaker) {
		
		if (goal.getPlan().getGoal().isPrimitive())
			collaboration.getDisco().getInteraction().occurred(speaker, goal.getPlan().getGoal(), contributes);
		else
			throw new IllegalArgumentException("Non-primitive tasks can not be executed!");
	}
}
