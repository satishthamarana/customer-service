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
    }
  stage('Clone repository') {
    steps {
      
			git 'https://github.com/satishthamarana/customer-service.git'
			
    
			
     }
			
  }
     
    stage('Build') {
      steps {
        dir ("customer-pipeline") {
           sh 'mvn -B -DskipTests clean package'
        }
           
	  
      }
    }

    stage('Docker Build image') {
      steps{
        dir ("customer-pipeline"){
          script {
          dockerImage = docker.build dockerimagename
        }
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

