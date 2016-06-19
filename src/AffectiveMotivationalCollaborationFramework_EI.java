import GUI.AMCFrame;
import Mechanisms.Collaboration.Collaboration;
import MetaInformation.MentalProcesses;
import MetaInformation.Voice;
import MetaInformation.World;
import edu.wpi.disco.Disco;

import java.io.IOException;

import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class AffectiveMotivationalCollaborationFramework_EI {

	private static MentalProcesses mentalProcesses;
	private static World world;
	private static Voice voice;
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
		
		voice = new Voice ("cmu-rms-hsmm");
		
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
		
		collaboration.setCollaborationVoice(voice);
		collaboration.setCollaborationWorld(world);
		collaboration.getWorld().setUserValence(0.0);
		
		disco.getInteraction().setOk(false);
		
		collaboration.getInteraction().start(true);
	}

}
