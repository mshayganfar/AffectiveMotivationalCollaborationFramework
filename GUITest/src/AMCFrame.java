import javax.swing.JFrame;

public class AMCFrame extends JFrame {
	
	private AMCPanel currentPanel;
	
	public AMCFrame(String caption) {
		super(caption);
		currentPanel = new AMCPanel();
		setupFrame();
	}

	private void setupFrame() {
		this.setContentPane(currentPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
	
	public static void main(String[] args) {
		
		AMCFrame test = new AMCFrame("Affective Motivational Collaboration Framework");
		test.setVisible(true);
	}
}
