package GUI;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	
	private enum EMOTION {POSITIVE, NEGATIVE, NEUTRAL};
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
	private JLabel humanEmotionLabel;
	private ButtonGroup humanEmotionGroup;
	private JRadioButton humanPositiveEmotion;
	private JRadioButton humanNeutralEmotion;
	private JRadioButton humanNegativeEmotion;
	private ImageIcon humanImage;
	private JLabel humanImageHolder;
	private JLabel turnHolder;
	private SpringLayout currentLayout;
//	private JComboBox<String> humanUttrancesComboBox;
//	private JLabel humanUtteranceLabel;
	
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
		humanEmotionLabel 	    = new JLabel("I feel:");
		humanPositiveEmotion    = new JRadioButton("Positive");
		humanNeutralEmotion     = new JRadioButton("Neutral");
		humanNegativeEmotion    = new JRadioButton("Negative");
		humanEmotionGroup 	    = new ButtonGroup();
		humanImage       		= new ImageIcon(new ImageIcon("images/person.png").getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
		humanImageHolder 		= new JLabel(humanImage);
		turnHolder				= new JLabel("Robot's Turn");
		currentLayout 		    = new SpringLayout();
//		humanUtteranceLabel     = new JLabel("You Say:");
//		humanUttrancesComboBox  = new JComboBox<String>();
		
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
		humanEmotionLabel.setFont(new Font("Verdana", Font.BOLD, 32));
		humanPositiveEmotion.setFont(new Font("Verdana", Font.BOLD, 22));
		humanNeutralEmotion.setFont(new Font("Verdana", Font.BOLD, 22));
		humanNegativeEmotion.setFont(new Font("Verdana", Font.BOLD, 22));
		humanEmotionGroup.add(humanPositiveEmotion);
		humanEmotionGroup.add(humanNeutralEmotion);
		humanEmotionGroup.add(humanNegativeEmotion);
		humanDoneButton.setFont(new Font("Verdana", Font.BOLD, 22));
		humanDoneButton.setEnabled(false);
		turnHolder.setFont(new Font("Verdana", Font.BOLD, 32));
		turnHolder.setForeground(new Color(255, 0, 0));
		turnHolder.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		humanImageHolder.setBorder(BorderFactory.createLineBorder(Color.black, 1));
//		humanUtteranceLabel.setFont(new Font("Verdana", Font.BOLD, 32));
//		humanUttrancesComboBox.setFont(new Font("Verdana", Font.BOLD, 22));
//		humanUttrancesComboBox.addItem("Select an option...");
//		humanUttrancesComboBox.addItem("Okay, go ahead and fix the connection.");
//		humanUttrancesComboBox.addItem("No, I don't think this is going to work.");
//		humanUttrancesComboBox.addItem("Why do you say that?");
//		humanUttrancesComboBox.setPreferredSize(new Dimension(600, 40));
		
		setBackground(UIManager.getColor("Label.background"));
		
		this.add(robotEmotionTextField);
		this.add(robotUtteranceTextArea);
		this.add(robotUtteranceLabel);
		this.add(robotEmotionLabel);
		this.add(robotEmotionImageHolder);
		this.add(robotImageHolder);
		this.add(separator);
		this.add(humanDoneButton);
		this.add(humanEmotionLabel);
		this.add(humanPositiveEmotion);
		this.add(humanNeutralEmotion);
		this.add(humanNegativeEmotion);
		this.add(humanImageHolder);
		this.add(turnHolder);
		this.setLayout(currentLayout);
//		this.add(humanUttrancesComboBox);
//		this.add(humanUtteranceLabel);
		
		currentLayout.putConstraint(SpringLayout.SOUTH, humanDoneButton, 0, SpringLayout.SOUTH, humanNegativeEmotion);
		currentLayout.putConstraint(SpringLayout.EAST, humanDoneButton, 550, SpringLayout.EAST, separator);
		
		currentLayout.putConstraint(SpringLayout.WEST, robotEmotionTextField, 40, SpringLayout.WEST, this);
		currentLayout.putConstraint(SpringLayout.EAST, robotEmotionTextField, -40, SpringLayout.WEST, robotEmotionImageHolder);
		currentLayout.putConstraint(SpringLayout.NORTH, robotEmotionTextField, 10, SpringLayout.SOUTH, robotEmotionLabel);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotEmotionLabel, 30, SpringLayout.SOUTH, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.WEST, robotEmotionLabel, 40, SpringLayout.WEST, this);
		
		currentLayout.putConstraint(SpringLayout.WEST, robotUtteranceLabel, 40, SpringLayout.WEST, this);
		currentLayout.putConstraint(SpringLayout.VERTICAL_CENTER, robotUtteranceLabel, 0, SpringLayout.VERTICAL_CENTER, this);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotUtteranceTextArea, 60, SpringLayout.NORTH, robotUtteranceLabel);
		currentLayout.putConstraint(SpringLayout.WEST, robotUtteranceTextArea, 40, SpringLayout.WEST, this);
		currentLayout.putConstraint(SpringLayout.EAST, robotUtteranceTextArea, -40, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanEmotionLabel, 500, SpringLayout.NORTH, this);
		currentLayout.putConstraint(SpringLayout.WEST, humanEmotionLabel, 60, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanPositiveEmotion, 20, SpringLayout.SOUTH, humanEmotionLabel);
		currentLayout.putConstraint(SpringLayout.NORTH, humanNeutralEmotion, 5, SpringLayout.SOUTH, humanPositiveEmotion);
		currentLayout.putConstraint(SpringLayout.NORTH, humanNegativeEmotion, 5, SpringLayout.SOUTH, humanNeutralEmotion);
		
		currentLayout.putConstraint(SpringLayout.WEST, humanNegativeEmotion, 60, SpringLayout.WEST, separator);
		currentLayout.putConstraint(SpringLayout.WEST, humanNeutralEmotion, 60, SpringLayout.WEST, separator);
		currentLayout.putConstraint(SpringLayout.WEST, humanPositiveEmotion, 60, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, separator, 0, SpringLayout.HORIZONTAL_CENTER, this);
		currentLayout.putConstraint(SpringLayout.NORTH, separator, 150, SpringLayout.NORTH, this);
		currentLayout.putConstraint(SpringLayout.SOUTH, separator, -20, SpringLayout.SOUTH, this);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotEmotionImageHolder, 30, SpringLayout.SOUTH, robotUtteranceTextArea);
		currentLayout.putConstraint(SpringLayout.EAST, robotEmotionImageHolder, -50, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.NORTH, robotImageHolder, 50, SpringLayout.NORTH, this);
//		(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4 - 200)
		currentLayout.putConstraint(SpringLayout.WEST, robotImageHolder, 90, SpringLayout.WEST, this);
		
		currentLayout.putConstraint(SpringLayout.NORTH, humanImageHolder, 50, SpringLayout.NORTH, this);
//		(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4 - 150)
		currentLayout.putConstraint(SpringLayout.WEST, humanImageHolder, 190, SpringLayout.WEST, separator);
		
		currentLayout.putConstraint(SpringLayout.NORTH, turnHolder, 60, SpringLayout.NORTH, this);
		currentLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, turnHolder, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
//		currentLayout.putConstraint(SpringLayout.VERTICAL_CENTER, humanUtteranceLabel, 0, SpringLayout.VERTICAL_CENTER, this);
//		currentLayout.putConstraint(SpringLayout.WEST, humanUtteranceLabel, 40, SpringLayout.WEST, separator);
		
//		currentLayout.putConstraint(SpringLayout.NORTH, humanUttrancesComboBox, 60, SpringLayout.NORTH, humanUtteranceLabel);
//		currentLayout.putConstraint(SpringLayout.WEST, humanUttrancesComboBox, 40, SpringLayout.WEST, separator);
		
		humanDoneButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (turn) {
					disableRobotComponents();
					enableHumanComponents();
					turn = false;
					setRobotEmotionText("POSITIVE");
				}
				else {
					disableHumanComponents();
					enableRobotComponents();
					turn = true;
					setRobotEmotionText("NEGATIVE");
				}
				
				EMOTION emotion = getHumanEmotion();
				// Write a code here that updates human's perceived emotion in the framework.
				// setRobotEmotionText(emotion.toString());
				
//				int humanSelectedUtteranceIndex;
//				if ((humanSelectedUtteranceIndex = getHumanSelectedUtterance()) == 0)
//					JOptionPane.showMessageDialog(null, "Please select what do you want to say!");
//				else {
//					// Write a code here that uses human's utterance to make a decision in the framework.
//					// setRobotEmotionText(humanSelectedUtterance);
//				}
			}
		});
		
		humanPositiveEmotion.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	humanDoneButton.setEnabled(true);
	        }
	    });
		
		humanNeutralEmotion.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	humanDoneButton.setEnabled(true);
	        }
	    });
		
		humanNegativeEmotion.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	humanDoneButton.setEnabled(true);
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
		turnHolder.setText("Your Turn");
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

		humanEmotionLabel.setEnabled(false);
		humanPositiveEmotion.setEnabled(false);
		humanNeutralEmotion.setEnabled(false);
		humanNegativeEmotion.setEnabled(false);
		humanImageHolder.setEnabled(false);
		turnHolder.setText("Robot's Turn");
		turnHolder.setForeground(new Color(255, 0, 0));
//		humanUtteranceLabel.setEnabled(false);
//		humanUttrancesComboBox.setEnabled(false);
//		humanDoneButton.setEnabled(false);
	}
	
	public void enableHumanComponents() {
		humanEmotionLabel.setEnabled(true);
		humanPositiveEmotion.setEnabled(true);
		humanNeutralEmotion.setEnabled(true);
		humanNegativeEmotion.setEnabled(true);
		humanImageHolder.setEnabled(true);
//		humanUtteranceLabel.setEnabled(true);
//		humanUttrancesComboBox.setEnabled(true);
//		humanDoneButton.setEnabled(true);
	}
	
	public void setEmoticon(EMOTION agentEmotion) {
		
		switch (agentEmotion) {
			case POSITIVE:
				robotEmotionImage.setImage(new ImageIcon("images/happy.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
				break;
			case NEGATIVE:
				robotEmotionImage.setImage(new ImageIcon("images/sad.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
				break;
			case NEUTRAL:
				robotEmotionImage.setImage(new ImageIcon("images/neutral.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
				break;
		}
	}
	
	public void setRobotUtterance(String robotUtterance) {
		robotUtteranceTextArea.setText(robotUtterance);
	}
	
	public void setRobotEmotionText(String robotEmotionTest) {
		robotEmotionTextField.setText(robotEmotionTest);
	}
	
	public EMOTION getHumanEmotion() {
		
		if (humanNegativeEmotion.isSelected()) {
			humanEmotionGroup.clearSelection();
			return EMOTION.NEGATIVE;
		}
		else if (humanPositiveEmotion.isSelected()) {
			humanEmotionGroup.clearSelection();
			return EMOTION.POSITIVE;
		}
		else if (humanNeutralEmotion.isSelected()) {
			humanEmotionGroup.clearSelection();
			return EMOTION.NEUTRAL;
		}
		else
			return null;
	}
	
//	public int getHumanSelectedUtterance() {
//		return humanUttrancesComboBox.getSelectedIndex();
//	}
//	
//	public void clearHumanUtterances() {
//		humanUttrancesComboBox.removeAllItems();
//		humanUttrancesComboBox.addItem("Select an option...");
//	}
//	
//	public void setHumanSelectedUtterance(String humanUtteranceOption) {
//		humanUttrancesComboBox.addItem(humanUtteranceOption);
//	}
}
