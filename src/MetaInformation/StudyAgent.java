package MetaInformation;

import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass.Input;
import edu.wpi.disco.Agent;

public class StudyAgent extends Agent{
	
	MentalProcesses mentalProcesses;
	
	public StudyAgent(String name) {
		super(name);
	}
	
	public void prepareAgent(MentalProcesses mentalProcesses) {
		this.mentalProcesses = mentalProcesses;
	}
	
	@Override
	public boolean isDefinedSlot(Plan plan, Input input) {
		return mentalProcesses.getCollaborationMechanism().isInputAvailable(plan, input);
	}
	
	@Override
	public Object getSlotValue(Plan plan, Input input) {
		return mentalProcesses.getCollaborationMechanism().getInputValue(plan, input.getName());
	}
}
