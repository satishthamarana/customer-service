pipeline {


  environment {
    dockerimagename = "thamarana/customerapp"
    dockerImage = ""
  }

  agent any
  tools { 
        maven 'maven3'
        jdk 'jdk-11'
  }

  stages {
    stage('Setup Workspace') {
	steps {
	   cleanWs()
	}

     steps {
      dir("application") {
			checkout scm
			script {
				git_rev_count = sh(script: "git rev-list --all --count", returnStdout: true).trim()
      }
    } 
			
     }
			

    stage('Build') {
      steps {
	  dir("application") {
            sh 'mvn -B -DskipTests clean package'
	  }
      }
    }

    stage('Docker Build image') {
      steps{
        script {
          dockerImage = docker.build dockerimagename
        }
      }
    }

    stage('Pushing Image') {
      environment {
               registryCredential = 'dockerhublogin'
           }
      steps{
        script {
          docker.withRegistry( 'https://registry.hub.docker.com', registryCredential ) {
            dockerImage.push("latest")
          }
        }
      }
    }

    stage('Deploying App to Kubernetes') {
      steps {
        script {
          kubernetesDeploy(configs: "deploymentservice.yml", kubeconfigId: "kubernetes")
        }
      }
    }

  }

}
