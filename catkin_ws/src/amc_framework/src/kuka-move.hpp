#include "ros/ros.h"

#include "control_msgs/FollowJointTrajectoryActionGoal.h" // for sending trajectories to youka
#include "control_msgs/FollowJointTrajectoryActionResult.h" // for waiting trajectory results
#include "control_msgs/JointTolerance.h" // for sending trajectories to youka
#include "trajectory_msgs/JointTrajectory.h" // for sending trajectories to youka
#include "trajectory_msgs/JointTrajectoryPoint.h" // for sending trajectories to youka
#include "actionlib/goal_id_generator.h" // for sending trajectories to youka
#include "brics_actuator/JointPositions.h"
#include "sensor_msgs/JointState.h"
#include "std_srvs/Empty.h"

#include <boost/thread/thread.hpp>
#include <boost/units/systems/si/length.hpp>
#include <boost/units/systems/si/plane_angle.hpp>
#include <boost/units/io.hpp>
#include <boost/units/systems/angle/degrees.hpp>
#include <boost/units/conversion.hpp>

#include <vector>
#include <iostream>

#include <amc_framework/configurationService.h>

class MoveKuka {

	private:

		void PublishGripperJointCommand(double& qGripper);

		//Wait Methods	
		void WaitForArmResult();
		
		void WaitForGripper(double target);

		void MoveArmAndWait(const std::vector< std::vector<double> >& traj, double dt);

		// Arm Moving Methods
		void MoveArm(const amc_framework::configurationService::Request &req);
		void MoveArm(const std::vector<double> &armConfig);

		void MoveArmHome();

		bool pickupPrepareControlSwitch();
		bool movePrepareControlSwitch();
		bool putDownPrepareControlSwitch();

		bool pickupRemoveLeftCover();
		bool moveRemoveLeftCover();
		bool putDownRemoveLeftCover();

		bool pickupRemoveRightCover();
		bool moveRemoveRightCover();
		bool putDownRemoveRightCover();

		bool pickupConnectAdaptor();
		bool moveConnectAdaptor();
		bool putDownConnectAdaptor();

		bool pickupUnrollCable();
		bool moveUnrollCable();
		bool putDownUnrollCable();

		bool pickupPlaceCable();
		bool movePlaceCable();
		bool putDownPlaceCable();

		bool pickupPlacePanel();
		bool movePlacePanel();
		bool putDownPlacePanel();

		bool pickupCheckPanelAttachment();
		bool moveCheckPanelAttachment();
		bool putDownCheckPanelAttachment();

		bool pickupCheckControlSwitch();
		bool moveCheckControlSwitch();
		bool putDownCheckControlSwitch();

		bool pickupCheckOutputCurrent();
		bool moveCheckOutputCurrent();
		bool putDownCheckOutputCurrent();

		bool CloseGripper();
		bool OpenGripper();

  public:

		bool armMoving;

		std::vector<double> q;
		std::vector<double> qArmHome;
		std::vector<double> qArmPosition;

		ros::Publisher armTrajectoryPublisher;
		ros::Publisher gripperPositionPublisher;

		std::vector <brics_actuator::JointValue> gripperJointPositions;

		// Subscribers
		void UpdateRobotPose(const sensor_msgs::JointState::ConstPtr& msg);

		void ArmResultListener(const control_msgs::FollowJointTrajectoryActionResult::ConstPtr& msg);

	  // Services
		bool Move(amc_framework::configurationService::Request  &req,
	            amc_framework::configurationService::Response &res);

		bool GoHome(std_srvs::Empty::Request  &req,
	            std_srvs::Empty::Response &res);

		bool OpenGripper(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool CloseGripper(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool prepareControlSwitch(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool removeLeftCover(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool removeRightCover(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool connectAdaptor(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool unrollCable(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool placeCable(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool placePanel(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool checkPanelAttachment(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool checkControlSwitch(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);

		bool checkOutputCurrent(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res);
};

void MoveKuka::ArmResultListener(const control_msgs::FollowJointTrajectoryActionResult::ConstPtr& msg) {

  if( msg->status.status == 3 ){
    std::cout << "Arm stopped moving!" << std::endl;
    armMoving = false;
  }
}

void MoveKuka::UpdateRobotPose(const sensor_msgs::JointState::ConstPtr& msg) {

 // make sure we're not reading the base wheel joint values
  if( strcmp(msg->name[0].c_str(), "arm_joint_1") == 0 ) {
	// Keep all the joint values
	for(int i=0; i<5; i++)
	  q[i] = msg->position[i];
  }
}

bool MoveKuka::prepareControlSwitch(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupPrepareControlSwitch();
	movePrepareControlSwitch();
	putDownPrepareControlSwitch();
	return true;
}

bool MoveKuka::removeLeftCover(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupRemoveLeftCover();
	moveRemoveLeftCover();
	putDownRemoveLeftCover();
	return true;
}

bool MoveKuka::removeRightCover(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupRemoveRightCover();
	moveRemoveRightCover();
	putDownRemoveRightCover();
	return true;
}

bool MoveKuka::connectAdaptor(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupConnectAdaptor();
	moveConnectAdaptor();
	putDownConnectAdaptor();
	return true;
}

bool MoveKuka::unrollCable(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupUnrollCable();
	moveUnrollCable();
	putDownUnrollCable();
	return true;
}

bool MoveKuka::placeCable(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupPlaceCable();
	movePlaceCable();
	putDownPlaceCable();
	return true;
}

bool MoveKuka::placePanel(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupPlacePanel();
	movePlacePanel();
	putDownPlacePanel();
	return true;
}

bool MoveKuka::checkPanelAttachment(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupCheckPanelAttachment();
	moveCheckPanelAttachment();
	putDownCheckPanelAttachment();
	return true;
}

bool MoveKuka::checkControlSwitch(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupCheckControlSwitch();
	moveCheckControlSwitch();
	putDownCheckControlSwitch();
	return true;
}

bool MoveKuka::checkOutputCurrent(std_srvs::Empty::Request &req,
	             std_srvs::Empty::Response &res) {
	
	pickupCheckOutputCurrent();
	moveCheckOutputCurrent();
	putDownCheckOutputCurrent();
	return true;
}

bool MoveKuka::movePrepareControlSwitch() {

	double move[] = {2.18, 1.6, -1.3, 2.6, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::moveRemoveLeftCover() {

	double move[] = {1.43, 1.2, -1.0, 2.7, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::moveRemoveRightCover() {

	double move[] = {2.03, 1.6, -1.5, 2.5, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::moveConnectAdaptor() {

	double move[] = {1.88, 1.2, -0.8, 2.2, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::moveUnrollCable() {

	double move[] = {1.72, 1.6, -1.2, 2.2, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::movePlaceCable() {

	double move[] = {1.58, 1.4, -0.8, 2.2, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::movePlacePanel() {

	double move[] = {1.4, 1.2, -0.8, 2.1, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::moveCheckPanelAttachment() {

	double move[] = {1.25, 1.2, -0.8, 2.2, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::moveCheckControlSwitch() {

	double move[] = {0.99, 1.2, -1.0, 2.5, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::moveCheckOutputCurrent() {

	double move[] = {0.76, 1.2, -0.8, 2.2, 2.9};
  std::vector<double> vecMove (move, move + sizeof(move) / sizeof(double) );

	MoveArm(vecMove);
}

bool MoveKuka::pickupPrepareControlSwitch() {
	
	double top[] = {3.4, 2.0, -1.0, 1.75, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {3.4, 2.0, -0.8, 1.75, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {3.4, 1.5, -0.8, 2.2, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::pickupRemoveLeftCover() {
	
	double top[] = {3.6, 2.0, -0.7, 1.45, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {3.6, 2.0, -0.7, 1.65, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {3.6, 1.2, -1.0, 2.7, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::pickupRemoveRightCover() {
	
	double top[] = {3.805, 2.0, -0.7, 1.45, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {3.805, 2.05, -0.7, 1.58, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {3.805, 1.2, -0.7, 2.4, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::pickupConnectAdaptor() {
	
	double top[] = {4.0, 2.0, -0.7, 1.45, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {4.0, 2.1, -0.7, 1.45, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {4.0, 1.2, -0.7, 2.4, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::pickupUnrollCable() {
	
	double top[] = {3.45, 2.2, -1.3, 1.75, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {3.45, 2.25, -1.2, 1.75, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {3.45, 1.8, -1.6, 2.6, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::pickupPlaceCable() {
	
	double top[] = {3.62, 2.2, -1.3, 1.75, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {3.62, 2.25, -1.17, 1.75, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {3.62, 1.8, -1.6, 2.6, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::pickupPlacePanel() {
	
	double top[] = {3.79, 2.2, -1.3, 1.75, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {3.79, 2.25, -1.17, 1.75, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {3.79, 1.6, -1.6, 2.8, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::pickupCheckPanelAttachment() {
	
	double top[] = {3.96, 2.2, -1.3, 1.75, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {3.96, 2.3, -1.28, 1.8, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {3.96, 1.6, -1.6, 2.8, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::pickupCheckControlSwitch() {
	
	double top[] = {3.48, 2.5, -1.8, 1.75, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {3.48, 2.7, -2.3, 2.4, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {3.48, 1.6, -1.6, 2.8, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::pickupCheckOutputCurrent() {
	
	double top[] = {3.63, 2.5, -1.8, 1.75, 2.9};
  std::vector<double> vecTop (top, top + sizeof(top) / sizeof(double) );

	double down[] = {3.63, 2.7, -2.25, 2.4, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {3.63, 1.6, -1.6, 2.8, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	OpenGripper();
	MoveArm(vecTop);
	MoveArm(vecDown);
	CloseGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::putDownPrepareControlSwitch() {

	double down[] = {2.18, 2.12, -1.15, 2.0, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {2.18, 1.2, -1.3, 2.6, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;	
}

bool MoveKuka::putDownRemoveLeftCover() {

	double down[] = {1.43, 2.1, -1.1, 2.0, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {1.43, 1.2, -1.0, 2.7, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;	
}

bool MoveKuka::putDownRemoveRightCover() {

	double down[] = {2.03, 2.52, -1.97, 2.37, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {2.03, 1.2, -1.5, 2.5, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;	
}

bool MoveKuka::putDownConnectAdaptor() {

	double down[] = {1.88, 2.23, -1.125, 1.7, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {1.88, 1.2, -0.8, 2.2, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;	
}

bool MoveKuka::putDownUnrollCable() {

	double down[] = {1.74, 2.545, -1.875, 2.0, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {1.72, 1.2, -1.2, 2.2, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;	
}

bool MoveKuka::putDownPlaceCable() {

	double down[] = {1.58, 2.545, -1.85, 2.05, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {1.58, 1.2, -0.8, 2.1, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;
}

bool MoveKuka::putDownPlacePanel() {

	double down[] = {1.4, 2.5, -1.74, 2.0, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {1.4, 1.2, -0.8, 2.1, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;	
}

bool MoveKuka::putDownCheckPanelAttachment() {

	double down[] = {1.25, 2.35, -1.55, 2.0, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {1.25, 1.2, -0.8, 2.2, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;	
}

bool MoveKuka::putDownCheckControlSwitch() {

	double down[] = {0.99, 2.15, -0.88, 1.45, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {0.99, 1.2, -1.0, 2.5, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;	
}

bool MoveKuka::putDownCheckOutputCurrent() {

	double down[] = {0.76, 2.05, -0.68, 1.5, 2.9};
  std::vector<double> vecDown (down, down + sizeof(down) / sizeof(double) );

	double up[] = {0.76, 1.2, -0.8, 2.2, 2.9};
  std::vector<double> vecUp (up, up + sizeof(up) / sizeof(double) );

	MoveArm(vecDown);
	OpenGripper();
	MoveArm(vecUp);
  return true;	
}

bool MoveKuka::Move(amc_framework::configurationService::Request &req,
            amc_framework::configurationService::Response &res)
{
  MoveArm(req);
  return true;
}

bool MoveKuka::GoHome(std_srvs::Empty::Request &req,
             std_srvs::Empty::Response &res)
{
  MoveArmHome();
  return true;
}

bool MoveKuka::OpenGripper() {
  double val = 0.0115;
  std::cout << "Opening gripper..." << std::endl;
  PublishGripperJointCommand(val);
  WaitForGripper(val);
  return true;
}

bool MoveKuka::OpenGripper(std_srvs::Empty::Request  &req,
             std_srvs::Empty::Response &res)
{
  double val = 0.0115;
  std::cout << "Opening gripper..." << std::endl;
  PublishGripperJointCommand(val);
  WaitForGripper(val);
  return true;
}

bool MoveKuka::CloseGripper() {
  double val = 0.0;
  PublishGripperJointCommand(val);
  WaitForGripper(val);
  return true;
}

bool MoveKuka::CloseGripper(std_srvs::Empty::Request  &req,
             std_srvs::Empty::Response &res) 
{
  double val = 0.0;
  PublishGripperJointCommand(val);
  WaitForGripper(val);
  return true;
}

void MoveKuka::WaitForArmResult() {

  std::cout << "Arm is moving..." << std::endl;

  while(armMoving) {
		boost::this_thread::sleep(boost::posix_time::milliseconds(1));
  }
}

void MoveKuka::MoveArmAndWait(const std::vector< std::vector<double> >& traj, double dt) {
    
  // traj's inner vector should have the length of arm-dof
  // traj's outer vector should have the length of goal points in the trajectory

  // Goal ID Generator
  actionlib::GoalIDGenerator idGenerator;
    
  // create joint trajectory message
  trajectory_msgs::JointTrajectory jt;
  // Note: Find a way to fill these in automatically instead of hard-coding.
  jt.header.frame_id = "base_link";
  jt.joint_names.push_back("arm_joint_1"); // Limits: [0.0, 5.89921]
  jt.joint_names.push_back("arm_joint_2"); // Limits: [0.0, 2.55]
  jt.joint_names.push_back("arm_joint_3"); // Limits: [-5.18363, 0.0]
  jt.joint_names.push_back("arm_joint_4"); // Limits: [0.0, 3.57793]
  jt.joint_names.push_back("arm_joint_5"); // Limits: [0.0, 5.84685]
    
  for (unsigned i=0; i < traj.size(); i++) {
      
    trajectory_msgs::JointTrajectoryPoint tp;
      
    std::cout << "Configuration for trajectory point " << i << std::endl;
      
    double dq; // delta-q, difference between configuration angles
    
    double v; // velocity of this joint for this trajectory point
      
    // Loop for however many joints the arm has
    for(unsigned j=0; j < traj[i].size(); j++) {	

      tp.positions.push_back(traj[i][j]);

      // If this is the first or the last trajectory point
      // then all joint velocities are zero.
      if( i == 0 || i == traj.size()-1)
				v = 0.0;
      else {
	  
				// difference between two configurations (i.e. angles) of this particular joint
				// traj[i+1][j] is where this joint wants to go next
				// traj[i][j] is where this joint is currently
				dq = traj[i+1][j] - traj[i][j];
	  
				// dt is the time we want the robot to take between configurations
				v =  dq/dt;
      }
	
      tp.velocities.push_back(v);
      tp.accelerations.push_back(0.0);	
    }
    ros::Duration t(dt*i);
    tp.time_from_start = t;
    jt.points.push_back(tp);
  }

  control_msgs::FollowJointTrajectoryGoal jtg;
  jtg.trajectory = jt;
  ros::Duration gtt(5.0);
  jtg.goal_time_tolerance = gtt;
  //control_msgs::JointTolerance jtol; // joint tolerance
  //control_msgs::JointTolerance gtol; // goal tolerance
    
  control_msgs::FollowJointTrajectoryActionGoal jtag;
  jtag.goal_id = idGenerator.generateID();
  // fill the header
  // jtg.header.seq = seq;
  // jtg.header.stamp.secs = 0; // secs;
  // jtg.header.stamp.nsecs = 0; // nsecs;
  jtag.header.frame_id = "base_link";  
  jtag.goal = jtg;
    
  armMoving = true;

  std::cout << "Moving the arm..." << std::endl;

  armTrajectoryPublisher.publish(jtag);

  std::cout << "Waiting for the arm actionlib result..." << std::endl;

  WaitForArmResult();
}

void MoveKuka::MoveArm(const std::vector<double> &armConfig) {

	qArmPosition[0] = armConfig.at(0);
  qArmPosition[1] = armConfig.at(1);
  qArmPosition[2] = armConfig.at(2);
  qArmPosition[3] = armConfig.at(3);
  qArmPosition[4] = armConfig.at(4);

  std::cout << "Starting to move the arm. " << std::endl;
  
	std::vector< std::vector<double> > here2home;
  std::vector<double> here;
  here.assign(q.begin(),q.begin()+5);
  std::vector<double> home = qArmPosition;
  here2home.push_back(here);
  here2home.push_back(home);
  MoveArmAndWait(here2home,5.0);
 
  std::cout << "Finished moving the arm." << std::endl;
}

void MoveKuka::MoveArm(const amc_framework::configurationService::Request &req) {

	qArmPosition[0] = req.arm_joint_1;
  qArmPosition[1] = req.arm_joint_2;
  qArmPosition[2] = req.arm_joint_3;
  qArmPosition[3] = req.arm_joint_4;
  qArmPosition[4] = req.arm_joint_5;

  std::cout << "Starting to move the arm. " << std::endl;
  
	std::vector< std::vector<double> > here2home;
  std::vector<double> here;
  here.assign(q.begin(),q.begin()+5);
  std::vector<double> home = qArmPosition;
  here2home.push_back(here);
  here2home.push_back(home);
  MoveArmAndWait(here2home,5.0);
 
  std::cout << "Finished moving the arm." << std::endl;
}

void MoveKuka::MoveArmHome() {

	qArmHome[0] = 0.0;
  qArmHome[1] = 0.0;
  qArmHome[2] = 0.0;
  qArmHome[3] = 0.0;
  qArmHome[4] = 0.0;

  std::cout << "Starting to move the arm to home. " << std::endl;

  std::vector< std::vector<double> > here2right;
  std::vector<double> here;
  here.assign(q.begin(),q.begin()+5);
  std::vector<double> home = qArmHome;
  here2right.push_back(here);
  here2right.push_back(home);
  MoveArmAndWait(here2right,5.0);

  std::cout << "Finished moving the arm to home." << std::endl;
}

void MoveKuka::PublishGripperJointCommand(double& qGripper) {

  brics_actuator::JointPositions command;
    
  gripperJointPositions[0].value = qGripper;
  gripperJointPositions[1].value = qGripper;
    
  command.positions = gripperJointPositions;

  gripperPositionPublisher.publish(command);
}

void MoveKuka::WaitForGripper(double target) {

  std::cout << "Waiting for gripper..." << std::endl;

  while ( gripperJointPositions[0].value >= target+0.000001 || gripperJointPositions[0].value <= target-0.000001 ) {
    std::cout << "target: " << q[5] << " , " << gripperJointPositions[0].value << std::endl;
    boost::this_thread::sleep(boost::posix_time::milliseconds(1));
  }

  std::cout << "Gripper done." << std::endl;
}
