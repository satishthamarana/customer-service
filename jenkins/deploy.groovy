pipeline {
	agent any
    stages {
        stage('Build on k8 ') {
            steps {           
                        sh 'pwd'
                        sh 'cp -R helm/* .'
		                sh 'ls -ltra'
                        sh 'pwd'
                        sh '/usr/local/bin/helm install customer-service customer --set image.repository=registry.hub.docker.com/thamarana/customer-service --set image.tag=28'

              			
            }           
        }
    }
}