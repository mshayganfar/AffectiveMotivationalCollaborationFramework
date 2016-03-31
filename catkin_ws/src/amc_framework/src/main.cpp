#include "kuka-move.hpp"

void UpdateRobotPose(const sensor_msgs::JointState::ConstPtr& msg) {

 // make sure we're not reading the base wheel joint values
  if( strcmp(msg->name[0].c_str(), "arm_joint_1") == 0 ) {
	// Keep all the joint values
	for(int i=0; i<5; i++)
	  q[i] = msg->position[i];
  }
}

void ArmResultListener(const control_msgs::FollowJointTrajectoryActionResult::ConstPtr& msg) {
  if( msg->status.status == 3 ){
    std::cout << "Arm stopped moving!" << std::endl;
    armMoving = false;
  }
}

int main(int argc, char** argv) {

	MoveKuka kukaObj;

  q.resize(5);
  qArmHome.resize(5);
  qArmRight.resize(5);
  qArmLeft.resize(5);
  gripperJointPositions.resize(2);

  gripperJointPositions[0].joint_uri = "gripper_finger_joint_l";
  gripperJointPositions[0].unit = boost::units::to_string(boost::units::si::meters);
        
  gripperJointPositions[1].joint_uri = "gripper_finger_joint_r";
  gripperJointPositions[1].unit = boost::units::to_string(boost::units::si::meters);

  ros::init(argc, argv, "mahni");
  ros::NodeHandle nh;
  ros::Subscriber subArmActionResult = nh.subscribe("arm_1/arm_controller/follow_joint_trajectory/result", 1000, &ArmResultListener);
  ros::Subscriber subJointStates = nh.subscribe("joint_states", 1000, UpdateRobotPose);
  
  armTrajectoryPublisher = nh.advertise<control_msgs::FollowJointTrajectoryActionGoal> ("arm_1/arm_controller/follow_joint_trajectory/goal",1);

  gripperPositionPublisher = nh.advertise<brics_actuator::JointPositions > ("arm_1/gripper_controller/position_command", 1);

  ros::ServiceServer serviceGoHome  = nh.advertiseService("go_home", &MoveKuka::GoHome, &kukaObj);
  ros::ServiceServer serviceGoRight = nh.advertiseService("go_right", &MoveKuka::GoRight, &kukaObj);
  ros::ServiceServer serviceGoLeft  = nh.advertiseService("go_left", &MoveKuka::GoLeft, &kukaObj);
  ros::ServiceServer serviceOpenGripper  = nh.advertiseService("open_gripper", &MoveKuka::OpenGripper, &kukaObj);
  ros::ServiceServer serviceCloseGripper = nh.advertiseService("close_gripper", &MoveKuka::CloseGripper, &kukaObj);

  ros::MultiThreadedSpinner spinner(4); // Use 4 threads
  spinner.spin(); // spin() will not return until the node has been shutdown

  return 0;
}


