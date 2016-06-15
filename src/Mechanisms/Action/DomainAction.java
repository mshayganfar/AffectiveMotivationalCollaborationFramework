package Mechanisms.Action;

public class DomainAction {

	private String actionLabel;
	
	public DomainAction(String actionLabel) {
		this.actionLabel = actionLabel;
	}
	
	void run() {
		System.out.println("I ran action: " + this.actionLabel);
	}
}
