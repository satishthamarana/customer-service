def service = 'customer-service'
def customImage
def dev_k8s_api_server
def latestTag

def getBranch(environment) {
   if (environment == 'DEV' || environment == 'QA') {
     return 'develop'
   } else {
     return 'develop'
   }
 }

 def getNamespace(environment) {
   if (environment == 'DEV' || environment == 'QA') {
     return 'customer-service'
   } else {
     return 'customer-service'
   }
 }

 currentBuild.displayName = "# ${BUILD_NUMBER} - ${BRANCH_NAME}"
 pipeline {
    agent any
  parameters {
      string (name: 'version', description: 'Build Version')
      choice (name: 'environment', choices: ["dev", "qa", "uat", "prod"], description: 'Deployment Environment')    
  }
  environment {
      targetedBranch = getBranch(env.ENVIRONMENT)
      targetedNamespace = getNamespace(env.ENVIRONMENT)
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    skipDefaultCheckout true
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
          currentBuild.displayName = "# ${BUILD_NUMBER} - ${BRANCH_NAME}"
          currentBuild.description = git_rev_count
        }
      }
    }



    }

 }