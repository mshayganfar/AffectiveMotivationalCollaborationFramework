package GUI;

import javax.swing.JFrame;

public class AMCFrame extends JFrame {
	
	private AMCPanel currentPanel;
	
	public AMCFrame(String caption, boolean AMCrun) {
		super(caption);
		currentPanel = new AMCPanel(AMCrun);
		setupFrame();
	}

	private void setupFrame() {
		this.setContentPane(currentPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
	
//	public static void main(String[] args) {
//		
//		AMCFrame frame = new AMCFrame("Affective Motivational Collaboration Framework");
//		frame.pack();
//		frame.setVisible(true);
//	}
	
	public AMCPanel getPanel() {
		return this.currentPanel;
	}
}
