#!/usr/bin/env groovy

pipeline {

  agent any

  stages {
    stage('Start') {
      steps {
        library 'netlogo-shared'
        sendNotifications('NetLogo/Tortoise', 'STARTED')
      }
    }

    stage('TestJVM') {
      steps {
        sh 'git submodule update --init --recursive'
        sh "./sbt tortoiseJVM/test:compile"
        sh "./sbt tortoiseJVM/test:fast"
        sh "./sbt tortoiseJVM/test:language"
        sh "./sbt tortoiseJVM/depend"
        junit 'jvm/target/test-reports/*.xml'
      }
    }

    stage('TestJS') {
      steps {
        sh "./sbt tortoiseJS/test:compile"
        sh "./sbt tortoiseJS/test:test"
        junit 'js/target/test-reports/*.xml'
      }
    }
  }

  post {
    always {
      library 'netlogo-shared'
      sendNotifications('NetLogo/Tortoise', currentBuild.result)
    }
  }
}
