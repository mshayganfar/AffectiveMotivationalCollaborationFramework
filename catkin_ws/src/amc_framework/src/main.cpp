#include "kuka-move.hpp"

int main(int argc, char** argv) {

	MoveKuka kukaObj;

  kukaObj.q.resize(5);
  kukaObj.qArmPosition.resize(5);
  kukaObj.qArmHome.resize(5);
  kukaObj.gripperJointPositions.resize(2);

  kukaObj.gripperJointPositions[0].joint_uri = "gripper_finger_joint_l";
  kukaObj.gripperJointPositions[0].unit = boost::units::to_string(boost::units::si::meters);
        
  kukaObj.gripperJointPositions[1].joint_uri = "gripper_finger_joint_r";
  kukaObj.gripperJointPositions[1].unit = boost::units::to_string(boost::units::si::meters);

  ros::init(argc, argv, "mahni");
  ros::NodeHandle nh;
  ros::Subscriber subArmActionResult = nh.subscribe("arm_1/arm_controller/follow_joint_trajectory/result", 1000, &MoveKuka::ArmResultListener, &kukaObj);
  ros::Subscriber subJointStates = nh.subscribe("joint_states", 1000, &MoveKuka::UpdateRobotPose, &kukaObj);
  
  kukaObj.armTrajectoryPublisher = nh.advertise< control_msgs::FollowJointTrajectoryActionGoal > ("arm_1/arm_controller/follow_joint_trajectory/goal",1);

  kukaObj.gripperPositionPublisher = nh.advertise< brics_actuator::JointPositions > ("arm_1/gripper_controller/position_command", 1);

  ros::ServiceServer serviceGoHome  = nh.advertiseService("move", &MoveKuka::Move, &kukaObj);
  ros::ServiceServer serviceGoRight = nh.advertiseService("go_home", &MoveKuka::GoHome, &kukaObj);

  ros::ServiceServer servicePrepareControlSwitch = nh.advertiseService("prepare_control_switch", &MoveKuka::prepareControlSwitch, &kukaObj);
	ros::ServiceServer serviceRemoveLeftCover = nh.advertiseService("remove_left_cover", &MoveKuka::removeLeftCover, &kukaObj);
  
	ros::ServiceServer serviceOpenGripper  = nh.advertiseService("open_gripper", &MoveKuka::OpenGripper, &kukaObj);
  ros::ServiceServer serviceCloseGripper = nh.advertiseService("close_gripper", &MoveKuka::CloseGripper, &kukaObj);

  ros::MultiThreadedSpinner spinner(4); // Use 4 threads
  spinner.spin(); // spin() will not return until the node has been shutdown

  return 0;
}


