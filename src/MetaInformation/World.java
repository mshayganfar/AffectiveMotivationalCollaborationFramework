package MetaInformation;

import GUI.AMCFrame;
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
	private static AMCFrame frame;
	
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
	
	public World(MentalProcesses mentalProcesses, AMCFrame frame) {
		this.mentalProcesses = mentalProcesses;
		this.frame = frame;
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
		DiscoActionsWrapper daw = new DiscoActionsWrapper(mentalProcesses, frame);
		
//		collaboration.getDisco().getInteraction().getConsole().setRespond(false);
		
		Task topTask = collaboration.getDisco().getTaskClass("InstallSolarPanel").newInstance();
		Plan topPlan = new Plan(topTask);
		topPlan.getGoal().setShould(true);
		collaboration.getDisco().push(topPlan);
		
		daw.proposeTaskShould(topPlan, false);
		daw.proposeTaskShould(topPlan.getChildren().get(0), false);
	}
	
	public static void giveTurnToRobot() {
		frame.getPanel().giveTurnToRobot();
	}
	
	public static void giveTurnToUser() {
		frame.getPanel().giveTurnToHuman();
	}
	
	public static void callSupervisor() {
		mentalProcesses.getActionMechanism().say("I will ask my supervisor to come and help us with this!");
		// Wait (15 seconds) for the supervisor to come and help.
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void goHome() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/go_home");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void openGripper() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/open_gripper");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeGripper() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/close_gripper");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void prepareControlSwitch() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/prepare_control_switch");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeLeftCover() {
		mentalProcesses.getActionMechanism().say("I can help you with this.");
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/remove_left_cover");
		} catch (Exception e) {
			e.printStackTrace();
		}
		mentalProcesses.getActionMechanism().say("Use this instead of yours.");
	}
	
	public static void removeRightCover() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/remove_right_cover");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void connectAdaptor() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/connect_adaptor");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void unrollCable() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/unroll_cable");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void placeCable() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/place_cable");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void placePanel() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/place_panel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkPanelAttachment() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/check_panel_attachment");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkControlSwitch() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/check_control_switch");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkOutputCurrent() {
		try {
			mentalProcesses.getCollaborationMechanism().getCollaborationROSbridge().callService("/check_output_current");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
