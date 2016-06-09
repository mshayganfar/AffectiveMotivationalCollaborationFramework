package MetaInformation;

public class World {
	
	public static enum WeldingTool { MY_WELDING_TOOL }
	public static enum RemovingCoverTool { USER_TOOL, AGENT_TOOL }
	public static enum USER_VALENCE { POSITIVE, NEGATIVE, NEUTRAL }
	
	private static USER_VALENCE userValence;
	
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
	
	public void setUserValence(double valenceValue) {
		
		if (valenceValue == 0)
			userValence = USER_VALENCE.NEUTRAL;
		else if (valenceValue > 0)
			userValence = USER_VALENCE.POSITIVE;
		else if (valenceValue < 0)
			userValence = USER_VALENCE.NEGATIVE;
	}
}
