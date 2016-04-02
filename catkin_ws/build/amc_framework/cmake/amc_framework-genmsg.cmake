# generated from genmsg/cmake/pkg-genmsg.cmake.em

message(STATUS "amc_framework: 0 messages, 1 services")

set(MSG_I_FLAGS "-Iactionlib_msgs:/opt/ros/hydro/share/actionlib_msgs/cmake/../msg;-Igeometry_msgs:/opt/ros/hydro/share/geometry_msgs/cmake/../msg;-Inav_msgs:/opt/ros/hydro/share/nav_msgs/cmake/../msg;-Istd_msgs:/opt/ros/hydro/share/std_msgs/cmake/../msg;-Icontrol_msgs:/opt/ros/hydro/share/control_msgs/cmake/../msg;-Itrajectory_msgs:/opt/ros/hydro/share/trajectory_msgs/cmake/../msg;-Ibrics_actuator:/opt/ros/hydro/share/brics_actuator/cmake/../msg;-Isensor_msgs:/opt/ros/hydro/share/sensor_msgs/cmake/../msg")

# Find all generators
find_package(gencpp REQUIRED)
find_package(genlisp REQUIRED)
find_package(genpy REQUIRED)

add_custom_target(amc_framework_generate_messages ALL)

#
#  langs = gencpp;genlisp;genpy
#

### Section generating for lang: gencpp
### Generating Messages

### Generating Services
_generate_srv_cpp(amc_framework
  "/home/mahni/catkin_ws/src/amc_framework/srv/configurationService.srv"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/amc_framework
)

### Generating Module File
_generate_module_cpp(amc_framework
  ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/amc_framework
  "${ALL_GEN_OUTPUT_FILES_cpp}"
)

add_custom_target(amc_framework_generate_messages_cpp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_cpp}
)
add_dependencies(amc_framework_generate_messages amc_framework_generate_messages_cpp)

# target for backward compatibility
add_custom_target(amc_framework_gencpp)
add_dependencies(amc_framework_gencpp amc_framework_generate_messages_cpp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS amc_framework_generate_messages_cpp)

### Section generating for lang: genlisp
### Generating Messages

### Generating Services
_generate_srv_lisp(amc_framework
  "/home/mahni/catkin_ws/src/amc_framework/srv/configurationService.srv"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/amc_framework
)

### Generating Module File
_generate_module_lisp(amc_framework
  ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/amc_framework
  "${ALL_GEN_OUTPUT_FILES_lisp}"
)

add_custom_target(amc_framework_generate_messages_lisp
  DEPENDS ${ALL_GEN_OUTPUT_FILES_lisp}
)
add_dependencies(amc_framework_generate_messages amc_framework_generate_messages_lisp)

# target for backward compatibility
add_custom_target(amc_framework_genlisp)
add_dependencies(amc_framework_genlisp amc_framework_generate_messages_lisp)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS amc_framework_generate_messages_lisp)

### Section generating for lang: genpy
### Generating Messages

### Generating Services
_generate_srv_py(amc_framework
  "/home/mahni/catkin_ws/src/amc_framework/srv/configurationService.srv"
  "${MSG_I_FLAGS}"
  ""
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/amc_framework
)

### Generating Module File
_generate_module_py(amc_framework
  ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/amc_framework
  "${ALL_GEN_OUTPUT_FILES_py}"
)

add_custom_target(amc_framework_generate_messages_py
  DEPENDS ${ALL_GEN_OUTPUT_FILES_py}
)
add_dependencies(amc_framework_generate_messages amc_framework_generate_messages_py)

# target for backward compatibility
add_custom_target(amc_framework_genpy)
add_dependencies(amc_framework_genpy amc_framework_generate_messages_py)

# register target for catkin_package(EXPORTED_TARGETS)
list(APPEND ${PROJECT_NAME}_EXPORTED_TARGETS amc_framework_generate_messages_py)



if(gencpp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/amc_framework)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${gencpp_INSTALL_DIR}/amc_framework
    DESTINATION ${gencpp_INSTALL_DIR}
  )
endif()
add_dependencies(amc_framework_generate_messages_cpp actionlib_msgs_generate_messages_cpp)
add_dependencies(amc_framework_generate_messages_cpp geometry_msgs_generate_messages_cpp)
add_dependencies(amc_framework_generate_messages_cpp nav_msgs_generate_messages_cpp)
add_dependencies(amc_framework_generate_messages_cpp std_msgs_generate_messages_cpp)
add_dependencies(amc_framework_generate_messages_cpp control_msgs_generate_messages_cpp)
add_dependencies(amc_framework_generate_messages_cpp trajectory_msgs_generate_messages_cpp)
add_dependencies(amc_framework_generate_messages_cpp brics_actuator_generate_messages_cpp)
add_dependencies(amc_framework_generate_messages_cpp sensor_msgs_generate_messages_cpp)
add_dependencies(amc_framework_generate_messages_cpp std_srvs_generate_messages_cpp)

if(genlisp_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/amc_framework)
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genlisp_INSTALL_DIR}/amc_framework
    DESTINATION ${genlisp_INSTALL_DIR}
  )
endif()
add_dependencies(amc_framework_generate_messages_lisp actionlib_msgs_generate_messages_lisp)
add_dependencies(amc_framework_generate_messages_lisp geometry_msgs_generate_messages_lisp)
add_dependencies(amc_framework_generate_messages_lisp nav_msgs_generate_messages_lisp)
add_dependencies(amc_framework_generate_messages_lisp std_msgs_generate_messages_lisp)
add_dependencies(amc_framework_generate_messages_lisp control_msgs_generate_messages_lisp)
add_dependencies(amc_framework_generate_messages_lisp trajectory_msgs_generate_messages_lisp)
add_dependencies(amc_framework_generate_messages_lisp brics_actuator_generate_messages_lisp)
add_dependencies(amc_framework_generate_messages_lisp sensor_msgs_generate_messages_lisp)
add_dependencies(amc_framework_generate_messages_lisp std_srvs_generate_messages_lisp)

if(genpy_INSTALL_DIR AND EXISTS ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/amc_framework)
  install(CODE "execute_process(COMMAND \"/usr/bin/python\" -m compileall \"${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/amc_framework\")")
  # install generated code
  install(
    DIRECTORY ${CATKIN_DEVEL_PREFIX}/${genpy_INSTALL_DIR}/amc_framework
    DESTINATION ${genpy_INSTALL_DIR}
  )
endif()
add_dependencies(amc_framework_generate_messages_py actionlib_msgs_generate_messages_py)
add_dependencies(amc_framework_generate_messages_py geometry_msgs_generate_messages_py)
add_dependencies(amc_framework_generate_messages_py nav_msgs_generate_messages_py)
add_dependencies(amc_framework_generate_messages_py std_msgs_generate_messages_py)
add_dependencies(amc_framework_generate_messages_py control_msgs_generate_messages_py)
add_dependencies(amc_framework_generate_messages_py trajectory_msgs_generate_messages_py)
add_dependencies(amc_framework_generate_messages_py brics_actuator_generate_messages_py)
add_dependencies(amc_framework_generate_messages_py sensor_msgs_generate_messages_py)
add_dependencies(amc_framework_generate_messages_py std_srvs_generate_messages_py)
