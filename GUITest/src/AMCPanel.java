import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class AMCPanel extends JPanel {
	
	private JButton userDoneButton;
	private JTextField robotEmotionTextField;
	private JTextArea robotUtteranceTextArea;
	private JLabel robotUtteranceLabel;
	private JLabel robotEmotionLabel;
	private JSeparator separator;
	private JComboBox<String> humanUttrancesComboBox;
	private JLabel humanUtteranceLabel;
	private JLabel humanEmotionLabel;
	private ButtonGroup humanEmotionGroup;
	private JRadioButton humanPositiveEmotion;
	private JRadioButton humanNeutralEmotion;
	private JRadioButton humanNegativeEmotion;
	private SpringLayout currentLayout;
	private ImageIcon robotEmotionImage;
	private JLabel robotEmotionImageHolder;
	
	public AMCPanel() {
		
		userDoneButton 		    = new JButton("Done");
		robotEmotionTextField   = new JTextField();
		robotUtteranceTextArea  = new JTextArea(10, 30);
		robotUtteranceLabel     = new JLabel("Robot Says:");
		robotEmotionLabel 	    = new JLabel("Robot feels:");
		currentLayout 		    = new SpringLayout();
		separator 			    = new JSeparator();
		humanUtteranceLabel     = new JLabel("You Say:");
		humanEmotionLabel 	    = new JLabel("You feel:");
		humanPositiveEmotion    = new JRadioButton("Positive");
		humanNeutralEmotion     = new JRadioButton("Neutral");
		humanNegativeEmotion    = new JRadioButton("Negative");
		humanUttrancesComboBox  = new JComboBox<String>();
		humanEmotionGroup 	    = new ButtonGroup();
		robotEmotionImage       = new ImageIcon(new ImageIcon("faces/neutral.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		robotEmotionImageHolder = new JLabel(robotEmotionImage);
		
		setupPanel();
	}
	
	private void setupPanel() {
		
		robotUtteranceTextArea.setLineWrap (true);
		robotUtteranceTextArea.setWrapStyleWord (true);
		userDoneButton.setFont(new Font("Verdana", Font.BOLD, 22));
		robotUtteranceTextArea.setFont(new Font("Verdana", Font.BOLD, 22));
		robotEmotionTextField.setFont(new Font("Verdana", Font.BOLD, 22));
		robotEmotionTextField.setColumns(20);
		robotUtteranceLabel.setFont(new Font("Verdana", Font.BOLD, 32));
		robotEmotionLabel.setFont(new Font("Verdana", Font.BOLD, 32));
		separator.setOrientation(SwingConstants.VERTICAL);
		humanUtteranceLabel.setFont(new Font("Verdana", Font.BOLD, 32));
		humanEmotionLabel.setFont(new Font("Verdana", Font.BOLD, 32));
		humanUttrancesComboBox.setFont(new Font("Verdana", Font.BOLD, 22));
		humanUttrancesComboBox.addItem("Okay, go ahead and fix the connection.");
		humanUttrancesComboBox.addItem("No, I don't think this is going to work.");
		humanUttrancesComboBox.addItem("Why do you say that?");
		humanPositiveEmotion.setFont(new Font("Verdana", Font.BOLD, 22));
		humanNeutralEmotion.setFont(new Font("Verdana", Font.BOLD, 22));
		humanNegativeEmotion.setFont(new Font("Verdana", Font.BOLD, 22));
		humanEmotionGroup.add(humanPositiveEmotion);
		humanEmotionGroup.add(humanNeutralEmotion);
		humanEmotionGroup.add(humanNegativeEmotion);
		
		setBackground(UIManager.getColor("Label.background"));
		
		this.add(userDoneButton);
		this.add(robotEmotionTextField);
		this.add(robotUtteranceTextArea);
		this.add(robotUtteranceLabel);
		this.add(robotEmotionLabel);
		this.add(separator);
		this.add(humanUttrancesComboBox);
		this.add(humanUtteranceLabel);
		this.add(humanEmotionLabel);
		this.add(humanPositiveEmotion);
		this.add(humanNeutralEmotion);
		this.add(humanNegativeEmotion);
		this.setLayout(currentLayout);
		this.add(robotEmotionImageHolder);
		
		currentLayout.putConstraint(SpringLayout.SOUTH, userDoneButton, -27, SpringLayout.SOUTH, this);
		currentLayout.putConstraint(SpringLayout.EAST, userDoneButton, -26, SpringLayout.EAST, this);
		
		currentLayout.putConstraint(SpringLayout.WEST, robotEmotionTextField, 0, SpringLayout.WEST, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.SOUTH, robotEmotionTextField, -93, SpringLayout.SOUTH, this);
		currentLayout.putConstraint(SpringLayout.NORTH, robotEmotionTextField, 9, SpringLayout.SOUTH, robotEmotionLabel);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotEmotionLabel, 28, SpringLayout.SOUTH, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.WEST, robotEmotionLabel, 0, SpringLayout.WEST, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.SOUTH, robotEmotionLabel, -138, SpringLayout.SOUTH, this);
		
		currentLayout.putConstraint(SpringLayout.WEST, robotUtteranceLabel, 20, SpringLayout.WEST, this);
		currentLayout.putConstraint(SpringLayout.SOUTH, robotUtteranceLabel, -6, SpringLayout.NORTH, robotUtteranceTextArea);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotUtteranceTextArea, -423, SpringLayout.SOUTH, this);
		currentLayout.putConstraint(SpringLayout.SOUTH, robotUtteranceTextArea, -196, SpringLayout.SOUTH, this);
		currentLayout.putConstraint(SpringLayout.WEST, robotUtteranceTextArea, 20, SpringLayout.WEST, this);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanEmotionLabel, 187, SpringLayout.SOUTH, humanUtteranceLabel);
		currentLayout.putConstraint(SpringLayout.WEST, humanEmotionLabel, 0, SpringLayout.WEST, humanUtteranceLabel);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanUtteranceLabel, 6, SpringLayout.NORTH, robotUtteranceLabel);
		currentLayout.putConstraint(SpringLayout.WEST, humanUtteranceLabel, 531, SpringLayout.EAST, robotUtteranceLabel);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanUttrancesComboBox, 4, SpringLayout.NORTH, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.WEST, humanUttrancesComboBox, 0, SpringLayout.WEST, humanUtteranceLabel);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanPositiveEmotion, 18, SpringLayout.SOUTH, humanEmotionLabel);
		currentLayout.putConstraint(SpringLayout.NORTH, humanNeutralEmotion, 6, SpringLayout.SOUTH, humanPositiveEmotion);
		currentLayout.putConstraint(SpringLayout.NORTH, humanNegativeEmotion, 6, SpringLayout.SOUTH, humanNeutralEmotion);
		
		currentLayout.putConstraint(SpringLayout.WEST, humanNegativeEmotion, 0, SpringLayout.WEST, humanUttrancesComboBox);
		currentLayout.putConstraint(SpringLayout.WEST, humanNeutralEmotion, 0, SpringLayout.WEST, humanUttrancesComboBox);
		currentLayout.putConstraint(SpringLayout.WEST, humanPositiveEmotion, 0, SpringLayout.WEST, humanUttrancesComboBox);
		
		currentLayout.putConstraint(SpringLayout.EAST, separator, 744, SpringLayout.WEST, this);
		currentLayout.putConstraint(SpringLayout.NORTH, separator, 28, SpringLayout.NORTH, this);
		currentLayout.putConstraint(SpringLayout.WEST, separator, 21, SpringLayout.EAST, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.SOUTH, separator, -6, SpringLayout.NORTH, userDoneButton);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotEmotionImageHolder, 30, SpringLayout.SOUTH, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.EAST, robotEmotionImageHolder, -50, SpringLayout.EAST, robotUtteranceTextArea);
	}
}
