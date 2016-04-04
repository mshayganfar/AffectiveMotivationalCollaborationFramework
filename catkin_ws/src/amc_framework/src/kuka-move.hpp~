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

		void MoveArmHome();

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

bool MoveKuka::Move(amc_framework::configurationService::Request &req,
            amc_framework::configurationService::Response &res)
{
  MoveArm(req);
  return true;
}

bool MoveKuka::GoHome(std_srvs::Empty::Request  &req,
             std_srvs::Empty::Response &res)
{
  MoveArmHome();
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

bool MoveKuka::CloseGripper(std_srvs::Empty::Request  &req,
             std_srvs::Empty::Response &res) {
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
  jt.joint_names.push_back("arm_joint_3"); // Limits: [0.0, 5.18363]
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
