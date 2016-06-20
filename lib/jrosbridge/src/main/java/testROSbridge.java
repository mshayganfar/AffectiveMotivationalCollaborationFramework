import javax.json.JsonArray;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Service;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;
import edu.wpi.rail.jrosbridge.services.ServiceResponse;

public class testROSbridge {

	String IP = "130.215.28.106";
	protected Ros ros;
	
	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public void rosConnect() throws Exception {
		if (ros == null) {
			ros = new Ros(IP/*, 48415*/);
		}

		if (!ros.isConnected()) {
			System.out.println("Connecting to ROS ...");
			if (!ros.connect()) {
				System.out.println("ERROR: connecting to ros failed.");
				throw new Exception();
			}
			System.out.println("Connection established.");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		ros.disconnect();
	}
	
	public boolean callService() throws Exception {

		Service planRequest = new Service(ros, "/close_gripper", "");
		ServiceRequest toPlan;
		toPlan = new ServiceRequest("{}");
		
		ServiceResponse response = new ServiceResponse();
		
		response = planRequest.callServiceAndWait(toPlan);
		
		return true;
	}
	
	public static void main(String[] args) {
		
		testROSbridge t = new testROSbridge();
		try {
			t.rosConnect();
			t.callService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
