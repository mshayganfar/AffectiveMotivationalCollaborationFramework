package MetaInformation;

import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass.Input;
import edu.wpi.disco.Agent;
import edu.wpi.disco.plugin.AuthorizedPlugin;

public class AMCAgent extends Agent{

	MentalProcesses mentalProcesses;
	
	public AMCAgent(String name) {
		super(name);
		new AuthorizedPlugin(agenda, 225);
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
