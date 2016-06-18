import GUI.AMCFrame;
import Mechanisms.Collaboration.Collaboration;
import MetaInformation.MentalProcesses;
import MetaInformation.World;
import edu.wpi.disco.Disco;
import javax.swing.JTextField;

public class AffectiveMotivationalCollaborationFramework_EI {

	private static MentalProcesses mentalProcesses;
	private static World world;
	
	public static void main(String[] args) {
		
		AMCFrame frame = new AMCFrame("Affective Motivational Collaboration Framework");
		frame.pack();
		frame.setVisible(true);
		frame.getPanel().giveTurnToRobot();
		((JTextField)frame.getPanel().getComponent("robotEmotionTextField")).setText("NEUTRAL");
		
		mentalProcesses = new MentalProcesses(args, frame, false);
		
		frame.getPanel().setMentalProcesses(mentalProcesses);
		
		world = new World(mentalProcesses, frame);
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		Disco disco = collaboration.getDisco();
		
		collaboration.setCollaborationWorld(world);
		collaboration.getWorld().setUserValence(0.0);
		
		disco.getInteraction().setOk(false);
		
		collaboration.getInteraction().start(true);
	}

}
