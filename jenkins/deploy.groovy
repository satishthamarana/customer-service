pipeline {
	agent any
    stages {
        stage('Build on k8 ') {
            steps {           
                        sh 'pwd'
                        sh 'cp -R helm/* .'
		        sh 'ls -ltr'
                        sh 'pwd'
                        sh '/usr/local/bin/helm upgrade --install customer-service --set image.repository=registry.hub.docker.com/thamarana/customer-service customer --set image.tag=28'

              			
            }           
        }
    }
}