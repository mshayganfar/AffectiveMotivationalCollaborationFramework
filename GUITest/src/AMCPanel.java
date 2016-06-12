import javax.swing.BorderFactory;
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class AMCPanel extends JPanel {
	
	private boolean turn = false;
	
	private JTextField robotEmotionTextField;
	private JTextArea robotUtteranceTextArea;
	private JLabel robotUtteranceLabel;
	private JLabel robotEmotionLabel;
	private ImageIcon robotEmotionImage;
	private JLabel robotEmotionImageHolder;
	private ImageIcon robotImage;
	private JLabel robotImageHolder;
	private JSeparator separator;
	private JButton humanDoneButton;
	private JComboBox<String> humanUttrancesComboBox;
	private JLabel humanUtteranceLabel;
	private JLabel humanEmotionLabel;
	private ButtonGroup humanEmotionGroup;
	private JRadioButton humanPositiveEmotion;
	private JRadioButton humanNeutralEmotion;
	private JRadioButton humanNegativeEmotion;
	private ImageIcon humanImage;
	private JLabel humanImageHolder;
	private JLabel turnHolder;
	private SpringLayout currentLayout;
	
	public AMCPanel() {
		
		robotEmotionTextField   = new JTextField();
		robotUtteranceTextArea  = new JTextArea(5, 37);
		robotUtteranceLabel     = new JLabel("Robot Says:");
		robotEmotionLabel 	    = new JLabel("Robot feels:");
		robotEmotionImage       = new ImageIcon(new ImageIcon("images/neutral.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		robotEmotionImageHolder = new JLabel(robotEmotionImage);
		robotImage       		= new ImageIcon(new ImageIcon("images/kuka.png").getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT));
		robotImageHolder 		= new JLabel(robotImage);
		separator 			    = new JSeparator();
		humanDoneButton 		= new JButton("Done");
		humanUtteranceLabel     = new JLabel("You Say:");
		humanEmotionLabel 	    = new JLabel("You feel:");
		humanPositiveEmotion    = new JRadioButton("Positive");
		humanNeutralEmotion     = new JRadioButton("Neutral");
		humanNegativeEmotion    = new JRadioButton("Negative");
		humanUttrancesComboBox  = new JComboBox<String>();
		humanEmotionGroup 	    = new ButtonGroup();
		humanImage       		= new ImageIcon(new ImageIcon("images/person.png").getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
		humanImageHolder 		= new JLabel(humanImage);
		turnHolder				= new JLabel("Robot's Turn");
		currentLayout 		    = new SpringLayout();
		
		setupPanel();
	}
	
	private void setupPanel() {
		
		robotUtteranceTextArea.setLineWrap (true);
		robotUtteranceTextArea.setWrapStyleWord (true);
		robotUtteranceTextArea.setFont(new Font("Verdana", Font.BOLD, 22));
		robotUtteranceTextArea.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		robotEmotionTextField.setFont(new Font("Verdana", Font.BOLD, 22));
		robotEmotionTextField.setColumns(20);
		robotEmotionTextField.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		robotUtteranceLabel.setFont(new Font("Verdana", Font.BOLD, 32));
		robotEmotionLabel.setFont(new Font("Verdana", Font.BOLD, 32));
		robotImageHolder.setBorder(BorderFactory.createLineBorder(Color.black, 1));
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
		humanDoneButton.setFont(new Font("Verdana", Font.BOLD, 22));
		turnHolder.setFont(new Font("Verdana", Font.BOLD, 32));
		turnHolder.setForeground(new Color(255, 0, 0));
		turnHolder.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		humanImageHolder.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		setBackground(UIManager.getColor("Label.background"));
		
		this.add(robotEmotionTextField);
		this.add(robotUtteranceTextArea);
		this.add(robotUtteranceLabel);
		this.add(robotEmotionLabel);
		this.add(robotEmotionImageHolder);
		this.add(robotImageHolder);
		this.add(separator);
		this.add(humanDoneButton);
		this.add(humanUttrancesComboBox);
		this.add(humanUtteranceLabel);
		this.add(humanEmotionLabel);
		this.add(humanPositiveEmotion);
		this.add(humanNeutralEmotion);
		this.add(humanNegativeEmotion);
		this.add(humanImageHolder);
		this.add(turnHolder);
		this.setLayout(currentLayout);
		
		currentLayout.putConstraint(SpringLayout.VERTICAL_CENTER, humanDoneButton, 350, SpringLayout.VERTICAL_CENTER, this);
		currentLayout.putConstraint(SpringLayout.EAST, humanDoneButton, 600, SpringLayout.EAST, separator);
		
		currentLayout.putConstraint(SpringLayout.WEST, robotEmotionTextField, 40, SpringLayout.WEST, this);
		currentLayout.putConstraint(SpringLayout.NORTH, robotEmotionTextField, 10, SpringLayout.SOUTH, robotEmotionLabel);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotEmotionLabel, 30, SpringLayout.SOUTH, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.WEST, robotEmotionLabel, 40, SpringLayout.WEST, this);
		
		currentLayout.putConstraint(SpringLayout.WEST, robotUtteranceLabel, 40, SpringLayout.WEST, this);
		currentLayout.putConstraint(SpringLayout.VERTICAL_CENTER, robotUtteranceLabel, 0, SpringLayout.VERTICAL_CENTER, this);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotUtteranceTextArea, 60, SpringLayout.NORTH, robotUtteranceLabel);
		currentLayout.putConstraint(SpringLayout.WEST, robotUtteranceTextArea, 40, SpringLayout.WEST, this);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanEmotionLabel, 150, SpringLayout.SOUTH, humanUtteranceLabel);
		currentLayout.putConstraint(SpringLayout.WEST, humanEmotionLabel, 40, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.VERTICAL_CENTER, humanUtteranceLabel, 0, SpringLayout.VERTICAL_CENTER, this);
		currentLayout.putConstraint(SpringLayout.WEST, humanUtteranceLabel, 40, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanUttrancesComboBox, 60, SpringLayout.NORTH, humanUtteranceLabel);
		currentLayout.putConstraint(SpringLayout.WEST, humanUttrancesComboBox, 40, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanPositiveEmotion, 20, SpringLayout.SOUTH, humanEmotionLabel);
		currentLayout.putConstraint(SpringLayout.NORTH, humanNeutralEmotion, 5, SpringLayout.SOUTH, humanPositiveEmotion);
		currentLayout.putConstraint(SpringLayout.NORTH, humanNegativeEmotion, 5, SpringLayout.SOUTH, humanNeutralEmotion);
		
		currentLayout.putConstraint(SpringLayout.WEST, humanNegativeEmotion, 40, SpringLayout.WEST, separator);
		currentLayout.putConstraint(SpringLayout.WEST, humanNeutralEmotion, 40, SpringLayout.WEST, separator);
		currentLayout.putConstraint(SpringLayout.WEST, humanPositiveEmotion, 40, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, separator, 0, SpringLayout.HORIZONTAL_CENTER, this);
		currentLayout.putConstraint(SpringLayout.NORTH, separator, 150, SpringLayout.NORTH, this);
		currentLayout.putConstraint(SpringLayout.SOUTH, separator, -20, SpringLayout.SOUTH, this);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotEmotionImageHolder, 30, SpringLayout.SOUTH, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.EAST, robotEmotionImageHolder, -50, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotImageHolder, 50, SpringLayout.NORTH, this);
		currentLayout.putConstraint(SpringLayout.WEST, robotImageHolder, 180, SpringLayout.WEST, this);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanImageHolder, 50, SpringLayout.NORTH, this);
		currentLayout.putConstraint(SpringLayout.WEST, humanImageHolder, 200, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.NORTH, turnHolder, 60, SpringLayout.NORTH, this);
		currentLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, turnHolder, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
		humanDoneButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (turn) {
					disableRobotComponents();
					enableHumanComponents();
					turn = false;
				}
				else {
					disableHumanComponents();
					enableRobotComponents();
					turn = true;
				}
			}
		});
	}
	
	public void disableRobotComponents() {
		robotEmotionTextField.setEnabled(false);
		robotEmotionTextField.setBackground(new Color(220, 220, 220));
		robotUtteranceTextArea.setEnabled(false);
		robotUtteranceTextArea.setBackground(new Color(220, 220, 220));
		robotUtteranceLabel.setEnabled(false);
		robotEmotionLabel.setEnabled(false);
		robotEmotionImageHolder.setEnabled(false);
		robotImageHolder.setEnabled(false);
		turnHolder.setText("Human's Turn");
		turnHolder.setForeground(new Color(0, 0, 255));
	}
	
	public void enableRobotComponents() {
		robotEmotionTextField.setEnabled(true);
		robotEmotionTextField.setBackground(new Color(255, 255, 255));
		robotUtteranceTextArea.setEnabled(true);
		robotUtteranceTextArea.setBackground(new Color(255, 255, 255));
		robotUtteranceLabel.setEnabled(true);
		robotEmotionLabel.setEnabled(true);
		robotEmotionImageHolder.setEnabled(true);
		robotImageHolder.setEnabled(true);
	}
	
	public void disableHumanComponents() {
		humanUtteranceLabel.setEnabled(false);
		humanEmotionLabel.setEnabled(false);
		humanUttrancesComboBox.setEnabled(false);
		humanPositiveEmotion.setEnabled(false);
		humanNeutralEmotion.setEnabled(false);
		humanNegativeEmotion.setEnabled(false);
//		humanDoneButton.setEnabled(false);
		humanImageHolder.setEnabled(false);
		turnHolder.setText("Robot's Turn");
		turnHolder.setForeground(new Color(255, 0, 0));
	}
	
	public void enableHumanComponents() {
		humanUtteranceLabel.setEnabled(true);
		humanEmotionLabel.setEnabled(true);
		humanUttrancesComboBox.setEnabled(true);
		humanPositiveEmotion.setEnabled(true);
		humanNeutralEmotion.setEnabled(true);
		humanNegativeEmotion.setEnabled(true);
//		humanDoneButton.setEnabled(true);
		humanImageHolder.setEnabled(true);
	}
}
