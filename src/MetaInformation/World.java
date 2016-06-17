package MetaInformation;

import Mechanisms.Action.DiscoActionsWrapper;
import Mechanisms.Collaboration.Collaboration;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.Task;

public class World {
	
	public static enum WeldingTool { MY_WELDING_TOOL }
	public static enum RemovingCoverTool { USER_TOOL, AGENT_TOOL }
	public static enum USER_VALENCE { POSITIVE, NEGATIVE, NEUTRAL }
	
	public static USER_VALENCE userValence;
	
	private static MentalProcesses mentalProcesses;
	
	public static final boolean post_PrepareControlSwitch = true;
	public static final boolean post_RemoveCover = true;
	public static final boolean post_ConnectAdaptor = true;
	public static final boolean post_PlacePanel = true;
	public static final boolean post_WeldPanel = true;
	public static final boolean post_CheckPanelAttachment = true;
	public static final boolean post_CheckWirings = true;
	public static final boolean post_CheckControlSwitch = true;
	public static final boolean post_CheckCascadingCells = true;
	public static final boolean post_CheckOutputCurrent = true;
	
	public World(MentalProcesses mentalProcesses) {
		this.mentalProcesses = mentalProcesses;
	}
	
	public static boolean getExecutionOutcome() {
		return mentalProcesses.getPerceptionMechanism().perceiveExecutionOutcome();
	}
	
	public USER_VALENCE getUserValence() {
		return userValence;
	}
	
	public void setUserValence(double valenceValue) {
		
		if (valenceValue == 0)
			userValence = USER_VALENCE.NEUTRAL;
		else if (valenceValue > 0)
			userValence = USER_VALENCE.POSITIVE;
		else if (valenceValue < 0)
			userValence = USER_VALENCE.NEGATIVE;
	}
	
	public static void start() {
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		DiscoActionsWrapper daw = new DiscoActionsWrapper(mentalProcesses);
		
//		collaboration.getDisco().getInteraction().getConsole().setRespond(false);
		
		Task topTask = collaboration.getDisco().getTaskClass("InstallSolarPanel").newInstance();
		Plan topPlan = new Plan(topTask);
		topPlan.getGoal().setShould(true);
		collaboration.getDisco().push(topPlan);
		
		daw.proposeTaskShould(topPlan, false);
		daw.proposeTaskShould(topPlan.getChildren().get(0), false);
	}
}
