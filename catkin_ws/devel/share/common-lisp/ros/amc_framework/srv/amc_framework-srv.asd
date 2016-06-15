
(cl:in-package :asdf)

(defsystem "amc_framework-srv"
  :depends-on (:roslisp-msg-protocol :roslisp-utils )
  :components ((:file "_package")
    (:file "configurationService" :depends-on ("_package_configurationService"))
    (:file "_package_configurationService" :depends-on ("_package"))
  ))