package MetaInformation;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Service;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;
import edu.wpi.rail.jrosbridge.services.ServiceResponse;

public class ROSbridge {
	String IP;
	protected Ros ros;
	
	public ROSbridge(String IP) {
		this.IP = IP;
	}
	
	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
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
			System.out.println("Connection established successfully.");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		ros.disconnect();
	}
	
	public boolean callService(String serviceName) throws Exception {

		ServiceRequest toPlan;

		// serviceName example: "/close_gripper"
		Service planRequest = new Service(ros, serviceName, "");
		
		//Empty JSON object, since I do not to pass any argument to the service.
		toPlan = new ServiceRequest("{}");
		
		ServiceResponse response = new ServiceResponse();
		
		// If I need service response in the future. 
		response = planRequest.callServiceAndWait(toPlan);
		
		return true;
	}
	
	public static void main(String[] args) {
		
		ROSbridge rosBridge = new ROSbridge("130.215.28.106");
		try {
			rosBridge.rosConnect();
			rosBridge.callService("/close_gripper");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
