package edu.wpi.htnlfd.communication;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonArray;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Service;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;
import edu.wpi.rail.jrosbridge.services.ServiceResponse;
import edu.wpi.disco.Disco;
import edu.wpi.disco.Interaction;
import edu.wpi.htnlfd.LearnAgent;
import edu.wpi.htnlfd.actionDetector.PrimitiveActionDetector;
import edu.wpi.htnlfd.domain.PhysObj;
import edu.wpi.htnlfd.exception.ROSConnectionFailed;
import edu.wpi.htnlfd.world.*;
import edu.wpi.htnlfd.world.CarWorld.UpdateCarWorld;

/**
 * This class is intended as a communication interface for Disco to connect with
 * Motion Planner (through servicecall) and abstract world model (through topic
 * subscription).
 * 
 * @author Jun Tang jtang@wpi.edu
 */
public class MotionPlannerCommunication extends Communication {
	private boolean lock_execution = false;
	private boolean lock_planning = false;
	private boolean lock_update = true;
	private LearnAgent learnAgent;
	private int counter = 1; // to be deleted, just for testing
	private World world;

	private JsonObject planChosen; // TODO to be determined according to

	// response(duration & reliability)

	public MotionPlannerCommunication(String ip, World world, LearnAgent learnAgent) {
		super(ip);
		this.world = world;
		this.learnAgent = learnAgent;
	}

	/**
	 * @param actionType
	 *            action to be planed for (unscrew, hang, unhang, etc.)
	 * @param objects
	 *            objects involved in the action (corresponding wheels, nuts,
	 *            etc.)
	 * @return True, if receiving positive planning response from motion
	 *         planner, False, otherwise
	 * @throws InterruptedException
	 * @throws ROSConnectionFailed
	 */
	public boolean planRequest(String actionType, String[] objects) throws InterruptedException, ROSConnectionFailed {

		System.out.println("Single plan Request");

		// check the connection to ROS, connect if not connected yet
		rosConnect();
		// form new request

		Service planRequest = new Service(ros, "/heres_how_planner/PlanningQuery", "heres_how_msgs/AttributivePlan");
		ServiceRequest toPlan;
		if (objects.length == 2) {
			toPlan = new ServiceRequest("{\"Request\":{\"Actions\":[{\"ActionType\":\"" + actionType
					+ "\",\"Objects\":[\"" + objects[0] + "\",\"" + objects[1] + "\"]}]}}");
		} else {
			toPlan = new ServiceRequest("{\"Request\":{\"Actions\":[{\"ActionType\":\"" + actionType
					+ "\",\"Objects\":[\"" + objects[0] + "\"]}]}}");
		}
		lock_planning = true;
		ServiceResponse response = new ServiceResponse(); // used to store the
		// result of
		// planning
		response = planRequest.callServiceAndWait(toPlan);
		// TODO to solve potential block because of loss of response
		JsonArray planArray;
		planArray = response.toJsonObject().getJsonObject("Response").getJsonArray("Attributes");
		// return false if gets an empty plan -- planning failed
		if (planArray.isNull(0)) {
			return false;
		}
		planChosen = planArray.getJsonObject(0).getJsonObject("Label");
		// TODO handle 'no-plan' error
		// System.out.println("plan done");
		return true; // TODO should be determined by the response.errorCode in
						// the future
	}

	public JsonArray planRequest(List<Entry<String, ArrayList<String>>> actions)
			throws InterruptedException, ROSConnectionFailed {

		System.out.println("List plan Request");

		// check the connection to ROS, connect if not connected yet
		rosConnect();
		// form new request

		Service planRequest = new Service(ros, "/heres_how_planner/PlanningQuery", "heres_how_msgs/AttributivePlan");
		String planString = "";
		ServiceRequest toPlan = null;
		planString += "{\"Request\":{\"Actions\":[";
		for (Entry<String, ArrayList<String>> action : actions) {
			String actionName = action.getKey();
			ArrayList<String> inputs = action.getValue();
			if (actions.indexOf(action) != actions.size() - 1) {
				if (inputs.size() == 2) {
					planString += "{\"ActionType\":\"" + actionName + "\",\"Objects\":[\"" + inputs.get(0) + "\",\""
							+ inputs.get(1) + "\"]},";
				} else {
					planString += "{\"ActionType\":\"" + actionName + "\",\"Objects\":[\"" + inputs.get(0) + "\"]},";
				}
			} else {
				if (inputs.size() == 2) {
					planString += "{\"ActionType\":\"" + actionName + "\",\"Objects\":[\"" + inputs.get(0) + "\",\""
							+ inputs.get(1) + "\"]}";
				} else {
					planString += "{\"ActionType\":\"" + actionName + "\",\"Objects\":[\"" + inputs.get(0) + "\"]}";
				}
			}
		}

		planString += "]}}";
		toPlan = new ServiceRequest(planString);
		lock_planning = true;
		ServiceResponse response = new ServiceResponse(); // used to store the
		// result of
		// planning
		response = planRequest.callServiceAndWait(toPlan);
		// TODO to solve potential block because of loss of response
		JsonArray planAttrs;
		planAttrs = response.toJsonObject().getJsonObject("Response").getJsonArray("Attributes");
		// return false if gets an empty plan -- planning failed
		if (planAttrs.isNull(0)) {
			return null;
		}
		// TODO handle 'no-plan' error
		// System.out.println("plan done");
		return planAttrs; // TODO should be determined by the response.errorCode
							// in
		// the future
	}

	private boolean waitForCallingDiscoToUpdateAfterUnhang = false;

	/**
	 * used to send execution request to motion planner, can be called only
	 * after calling planRequest().
	 * 
	 * @param label
	 *            the data structure to represent the chosen "plan"
	 * @return True, if receive success response from motion planner False,
	 *         otherwise
	 * @throws InterruptedException
	 * @throws ROSConnectionFailed
	 */
	public boolean executeRequest(int label, int debug, Object... objects)
			throws InterruptedException, ROSConnectionFailed {

		// check the connection to ROS, connect if not connected yet
		rosConnect();

		Service executeRequest = null;
		ServiceRequest toExecute = null;
		if (learnAgent.getDisco().getGlobal("$world") instanceof CarWorld) {
			executeRequest = new Service(ros, "/heres_how_planner/ExecutionQuery", "heres_how_msgs/ExecutePlan");

			JsonObject newPlanChosen = Json.createObjectBuilder().add("debug", debug)
					.add("sourceAction", planChosen.get("sourceAction")).add("version", planChosen.get("version"))
					.add("targetAction", planChosen.get("targetAction")).build();
			// System.out.println(newPlanChosen.toString());

			toExecute = new ServiceRequest("{\"Labels\":[" + newPlanChosen.toString() + "]}");
		} else if (learnAgent.getDisco().getGlobal("$world") instanceof BlocksWorld) {
			executeRequest = new Service(ros, "/PracticeQuery", "simulation/heres_how_practice/PracticeQuery");

			JsonObject newPlanChosen = null;

			if (objects != null && objects.length == 1) {
				newPlanChosen = Json.createObjectBuilder().add("command_type", label)
						.add("src_object", objects[0].toString()).build();
			} else if (objects != null && objects.length == 2) {
				newPlanChosen = Json.createObjectBuilder().add("command_type", label)
						.add("src_object", objects[0].toString()).add("dst_object", objects[1].toString()).build();
			}

			// System.out.println(newPlanChosen.toString());

			toExecute = new ServiceRequest(newPlanChosen.toString());
		}

		lock_execution = true;
		ServiceResponse response = new ServiceResponse();
		response = executeRequest.callServiceAndWait(toExecute);
		// TODO to resolve potential block because of loss of response
		boolean exeStatus = false;
		if (learnAgent.getWorld() instanceof CarWorld) {
			exeStatus = response.toJsonObject().getJsonArray("ErrorCode").getString(0).equals("NO ERROR in EXECUTION");
			// System.out.println(response.toJsonObject());
		} else if (learnAgent.getWorld() instanceof BlocksWorld) {
			exeStatus = response.toJsonObject().getBoolean("success");
		}
		if (!exeStatus) {
			System.out.println("ERROR: execution error--" + response.toString() + "Communication.executeRequest()");
			return false;
		}
		// wait for world state to change or time out
		int countDown = 10;
		int countDownStep = 100;
		while (waitForCallingDiscoToUpdateAfterUnhang && countDown > 0) {
			Thread.sleep(countDownStep);
			countDown--;
		}
		return true;
	}

	/**
	 * used to subscribe to the ROS messages (world states publised by abstract
	 * world model from within ROS).
	 * 
	 * @param topic
	 *            the name of the topic to subscribe
	 * @param type
	 *            the type of the topic to subscribe
	 * @return
	 * @throws ROSConnectionFailed
	 */
	public boolean subscribeUpdates(String topic, String type) throws ROSConnectionFailed {

		// check the connection to ROS, connect if not connected yet
		rosConnect();

		Topic messageResource = new Topic(ros, topic, type);

		PrimitiveActionDetector primDetect = new PrimitiveActionDetector(learnAgent);
		learnAgent.getDisco().setGlobal("$detector",primDetect);
		
		messageResource.subscribe(new TopicCallback() {
			
			Semaphore mutex = new Semaphore(1, true);;

			@Override
			public void handleMessage(Message message) {

				/*
				 * try { mutex.acquire(); } catch (InterruptedException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 */

				Map<String, String> onObjects = new HashMap<String, String>();
				List<String> looseNuts = new ArrayList<String>();
				List<String> onTable = new ArrayList<String>();
				String holding = null;

				JsonArray jArray = message.toJsonObject().getJsonArray("states");

				JsonObject jObject;
				counter++;
				World.UpdateWorld update = null;
				if (world instanceof CarWorld) {
					for (int i = 0; i < jArray.size(); i++) {
						jObject = jArray.getJsonObject(i);
						switch ((String) jObject.getString("condition_name")) {
						case "on_hub":
							onObjects.put(jObject.getString("condition_parameter") // hub_LF,etc
							, jObject.getString("string_condition")); // wheel_LF,etc
						case "on_stud":
							onObjects.put(jObject.getString("condition_parameter") // stud_LF_1,etc
							, jObject.getString("string_condition")); // nut_LF_1,etc
							break;
						case "holding":
							holding = jObject.getString("string_condition"); // wheel_LF,nut_LF_1,etc
							break;
						case "loose_nut":
							looseNuts.add(jObject.getString("string_condition")); // nut_LF_1,etc
							break;
						case "loose_wheel":
							onObjects.put("loose_wheel", jObject.getString("condition_parameter")); // wheel_LF,etc
							break;
						default:
							// System.out
							// .println("ERROR: unknown condition --
							// Communication.subscribeUpdates(): "
							// + (String) jObject.getString("cond
							// primDetect.enqueueUpdate((UpdateCarWorld)
							// updaition_name"));
						}
					}
					update = ((CarWorld) world).new UpdateCarWorld(onObjects, looseNuts, holding,
							learnAgent.getDisco());

					// System.out.println("~~~~~ Update Info: ");
					// System.out.println(" onObjects: " + onObjects.size());
					// System.out.println(" holding: " + holding);
					// System.out.println(" looseNuts: " + looseNuts.size());
					// System.out.println("Nuts in Communication:");
					// for(String x : looseNuts) {
					// System.out.println(" " + x);
					// }

					// ////////////////////////////////////////////////////////
					// Dan's Primitive Recognition code
					// --> This is trying to see, given things happening (nut is on stud! nut is not on stud...) 
					// what action just occcured. (THis was originally to make it so a user wouldn't have to keep pressing 
					// buttons to say what action they just did.) 
					// NOt using this anymore. (Probably). 
					// ////////////////////////////////////////////////////////

					/*primDetect.enqueueUpdate(primDetect.new UpdateParameters(onObjects, looseNuts, holding));
					primDetect.run();*/

				} else if (world instanceof BlocksWorld) {
					for (int i = 0; i < jArray.size(); i++) {

						jObject = jArray.getJsonObject(i);
						switch ((String) jObject.getString("condition_name")) {
						case "on_block":
							onObjects.put(jObject.getString("condition_parameter"),
									jObject.getString("string_condition"));
							break;
						case "on_table":
							onTable.add(jObject.getString("condition_parameter")); // nut_LF_1,etc
							break;
						case "holding":
							holding = jObject.getString("string_condition");

							break;
						default:
							System.out.println("ERROR: unknown condition -- Communication.subscribeUpdates()");
						}
					}
					update = ((BlocksWorld) world).new UpdateBlocksWorld(onObjects, onTable, holding,
							learnAgent.getDisco());
				}
				world.setNextUpdate(update);
				update.setTimestamp(counter);
				learnAgent.getDisco().onTick.run();
				System.out.println("WE ARE GETTING UPDATES FROM ABSTRACT WORLD MODEL");
				waitForCallingDiscoToUpdateAfterUnhang = false; // TODO to
				// be
				// deleted
				// mutex.release();
			}

		});
		return true;
	}

	/**
	 * used to check the status of Connection
	 * 
	 * @param lock
	 *            the connection to be checked
	 * @return True, if the connection is blocked (waiting for response) False,
	 *         otherwise
	 */
	public boolean isLocked(String lock) {
		boolean result = false;
		switch (lock) {
		case "execution":
			result = lock_execution;
			break;
		case "planning":
			result = lock_planning;
			break;
		case "update":
			result = lock_update;
			break;
		}
		return result;

	}

	/**
	 * used to connect to ROS, called manually when intend to use ROS mode; not
	 * called when running Disco-only return
	 * 
	 * @return True if connect successfully, false otherwise
	 */
	public boolean connect() {
		ros = new Ros(IP /* , 8080 */);
		return ros.connect();
	}

	/**
	 * subscribe to the abstract world model topic
	 * 
	 * @return True, if subscribe successfully False, otherwise
	 * @throws ROSConnectionFailed
	 */
	public boolean subscribe() throws ROSConnectionFailed {
		return subscribeUpdates("/abstract_world_model/world", "heres_how_symbolic_model/AbstractWorld");
	}

	/**
	 * check if is connected to ROS
	 * 
	 * @return True if yes, False otherwise
	 */
	public boolean isRosConnected() {
		if (ros == null) {
			return false;
		}
		return ros.isConnected();
	}
}