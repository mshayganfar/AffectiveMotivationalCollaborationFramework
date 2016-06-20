package edu.wpi.htnlfd.communication;

import edu.wpi.htnlfd.exception.ROSConnectionFailed;
import edu.wpi.rail.jrosbridge.Ros;

public class Communication {
	String IP = "localhost";
	protected Ros ros;

	public Communication(String ip) {
		if (ip != null)
			this.IP = ip;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public void rosConnect() throws ROSConnectionFailed {
		if (ros == null) {
			ros = new Ros(IP /* , 8080 */);
		}

		if (!ros.isConnected()) {
			System.out.println("Connecting to ROS ...");
			if (!ros.connect()) {
				System.out.println("ERROR: connecting to ros failed.");
				throw new ROSConnectionFailed();
			}
			System.out.println("Connection established.");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		ros.disconnect();
	}
}
