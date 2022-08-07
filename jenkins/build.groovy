def SERVICE = 'customer-rating'
def customImage
def latestTag

def getProfile() {
  if(env.BRANCH_NAME!="develop"){
     return "prod"
  }else{
     return "dev"
  }
}

pipeline {
    agent any
    environment {
    profile = getProfile();
}
    options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    skipDefaultCheckout true
  } 

    parameters {
    booleanParam (name: 'build_only', defaultValue: false, description: '' )
  } 
  tools { 
        jdk 'jdk-11'
        maven 'maven3'
        
    }
    stages {
        stage('Setup Workspace') {
      steps {
        cleanWs()
      }
    
    }
    stage('Checkout SCM') {
      steps {
       git 'https://github.com/satishthamarana/customer-service.git'
        script {
          git_rev_count = sh(script: "git rev-list --all --count", returnStdout: true).trim()  
          latestTag = git_rev_count 
          currentBuild.displayName = "# ${BUILD_NUMBER}-${BRANCH_NAME}"
          currentBuild.description = git_rev_count
        }
      }
    }
    stage('Build & Package') {
      steps {
        script {
          sh "pwd"
          sh("mvn -Dintegration-tests.skip=true -Dbuild.number=${BUILD_NUMBER} clean package")
        }
      }
    }
    stage('Copy Artifact') {
      steps { 
        script {
           sh 'pwd'
		       sh 'cp -r target/*.jar .'
            }
                
           }
       }
    stage('Docker Build') {
      steps {
        script {
          customImage = docker.build("thamarana/${SERVICE}:${latestTag}", ".")         
        }
      }
    }
    stage('Docker DEV Push') {
     steps {
         script {
            docker.withRegistry("https://registry.hub.docker.com", "registryCredential") {
               customImage.push("${latestTag}")
               sh("docker rmi -f thamarana/customer-rating:${latestTag}")
            }
       }
      }
    }

    
}
    post {
    always {           
        cleanWs()
    }       
  }


}