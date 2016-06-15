; Auto-generated. Do not edit!


(cl:in-package amc_framework-srv)


;//! \htmlinclude configurationService-request.msg.html

(cl:defclass <configurationService-request> (roslisp-msg-protocol:ros-message)
  ((arm_joint_1
    :reader arm_joint_1
    :initarg :arm_joint_1
    :type cl:float
    :initform 0.0)
   (arm_joint_2
    :reader arm_joint_2
    :initarg :arm_joint_2
    :type cl:float
    :initform 0.0)
   (arm_joint_3
    :reader arm_joint_3
    :initarg :arm_joint_3
    :type cl:float
    :initform 0.0)
   (arm_joint_4
    :reader arm_joint_4
    :initarg :arm_joint_4
    :type cl:float
    :initform 0.0)
   (arm_joint_5
    :reader arm_joint_5
    :initarg :arm_joint_5
    :type cl:float
    :initform 0.0))
)

(cl:defclass configurationService-request (<configurationService-request>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <configurationService-request>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'configurationService-request)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name amc_framework-srv:<configurationService-request> is deprecated: use amc_framework-srv:configurationService-request instead.")))

(cl:ensure-generic-function 'arm_joint_1-val :lambda-list '(m))
(cl:defmethod arm_joint_1-val ((m <configurationService-request>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader amc_framework-srv:arm_joint_1-val is deprecated.  Use amc_framework-srv:arm_joint_1 instead.")
  (arm_joint_1 m))

(cl:ensure-generic-function 'arm_joint_2-val :lambda-list '(m))
(cl:defmethod arm_joint_2-val ((m <configurationService-request>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader amc_framework-srv:arm_joint_2-val is deprecated.  Use amc_framework-srv:arm_joint_2 instead.")
  (arm_joint_2 m))

(cl:ensure-generic-function 'arm_joint_3-val :lambda-list '(m))
(cl:defmethod arm_joint_3-val ((m <configurationService-request>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader amc_framework-srv:arm_joint_3-val is deprecated.  Use amc_framework-srv:arm_joint_3 instead.")
  (arm_joint_3 m))

(cl:ensure-generic-function 'arm_joint_4-val :lambda-list '(m))
(cl:defmethod arm_joint_4-val ((m <configurationService-request>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader amc_framework-srv:arm_joint_4-val is deprecated.  Use amc_framework-srv:arm_joint_4 instead.")
  (arm_joint_4 m))

(cl:ensure-generic-function 'arm_joint_5-val :lambda-list '(m))
(cl:defmethod arm_joint_5-val ((m <configurationService-request>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader amc_framework-srv:arm_joint_5-val is deprecated.  Use amc_framework-srv:arm_joint_5 instead.")
  (arm_joint_5 m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <configurationService-request>) ostream)
  "Serializes a message object of type '<configurationService-request>"
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'arm_joint_1))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'arm_joint_2))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'arm_joint_3))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'arm_joint_4))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
  (cl:let ((bits (roslisp-utils:encode-double-float-bits (cl:slot-value msg 'arm_joint_5))))
    (cl:write-byte (cl:ldb (cl:byte 8 0) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 16) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 24) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 32) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 40) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 48) bits) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 56) bits) ostream))
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <configurationService-request>) istream)
  "Deserializes a message object of type '<configurationService-request>"
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'arm_joint_1) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'arm_joint_2) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'arm_joint_3) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'arm_joint_4) (roslisp-utils:decode-double-float-bits bits)))
    (cl:let ((bits 0))
      (cl:setf (cl:ldb (cl:byte 8 0) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 16) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 24) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 32) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 40) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 48) bits) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 56) bits) (cl:read-byte istream))
    (cl:setf (cl:slot-value msg 'arm_joint_5) (roslisp-utils:decode-double-float-bits bits)))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<configurationService-request>)))
  "Returns string type for a service object of type '<configurationService-request>"
  "amc_framework/configurationServiceRequest")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'configurationService-request)))
  "Returns string type for a service object of type 'configurationService-request"
  "amc_framework/configurationServiceRequest")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<configurationService-request>)))
  "Returns md5sum for a message object of type '<configurationService-request>"
  "1560ce9ccf0328d1834c5ac5ba0dc6ba")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'configurationService-request)))
  "Returns md5sum for a message object of type 'configurationService-request"
  "1560ce9ccf0328d1834c5ac5ba0dc6ba")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<configurationService-request>)))
  "Returns full string definition for message of type '<configurationService-request>"
  (cl:format cl:nil "float64 arm_joint_1~%float64 arm_joint_2~%float64 arm_joint_3~%float64 arm_joint_4~%float64 arm_joint_5~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'configurationService-request)))
  "Returns full string definition for message of type 'configurationService-request"
  (cl:format cl:nil "float64 arm_joint_1~%float64 arm_joint_2~%float64 arm_joint_3~%float64 arm_joint_4~%float64 arm_joint_5~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <configurationService-request>))
  (cl:+ 0
     8
     8
     8
     8
     8
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <configurationService-request>))
  "Converts a ROS message object to a list"
  (cl:list 'configurationService-request
    (cl:cons ':arm_joint_1 (arm_joint_1 msg))
    (cl:cons ':arm_joint_2 (arm_joint_2 msg))
    (cl:cons ':arm_joint_3 (arm_joint_3 msg))
    (cl:cons ':arm_joint_4 (arm_joint_4 msg))
    (cl:cons ':arm_joint_5 (arm_joint_5 msg))
))
;//! \htmlinclude configurationService-response.msg.html

(cl:defclass <configurationService-response> (roslisp-msg-protocol:ros-message)
  ()
)

(cl:defclass configurationService-response (<configurationService-response>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <configurationService-response>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'configurationService-response)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name amc_framework-srv:<configurationService-response> is deprecated: use amc_framework-srv:configurationService-response instead.")))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <configurationService-response>) ostream)
  "Serializes a message object of type '<configurationService-response>"
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <configurationService-response>) istream)
  "Deserializes a message object of type '<configurationService-response>"
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<configurationService-response>)))
  "Returns string type for a service object of type '<configurationService-response>"
  "amc_framework/configurationServiceResponse")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'configurationService-response)))
  "Returns string type for a service object of type 'configurationService-response"
  "amc_framework/configurationServiceResponse")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<configurationService-response>)))
  "Returns md5sum for a message object of type '<configurationService-response>"
  "1560ce9ccf0328d1834c5ac5ba0dc6ba")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'configurationService-response)))
  "Returns md5sum for a message object of type 'configurationService-response"
  "1560ce9ccf0328d1834c5ac5ba0dc6ba")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<configurationService-response>)))
  "Returns full string definition for message of type '<configurationService-response>"
  (cl:format cl:nil "~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'configurationService-response)))
  "Returns full string definition for message of type 'configurationService-response"
  (cl:format cl:nil "~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <configurationService-response>))
  (cl:+ 0
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <configurationService-response>))
  "Converts a ROS message object to a list"
  (cl:list 'configurationService-response
))
(cl:defmethod roslisp-msg-protocol:service-request-type ((msg (cl:eql 'configurationService)))
  'configurationService-request)
(cl:defmethod roslisp-msg-protocol:service-response-type ((msg (cl:eql 'configurationService)))
  'configurationService-response)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'configurationService)))
  "Returns string type for a service object of type '<configurationService>"
  "amc_framework/configurationService")