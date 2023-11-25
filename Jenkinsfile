pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying..'
                // Add your deployment script here
            }
        }
    }
    post {
        success {
            echo 'Build and Test Stages Successful!'
        }
        failure {
            echo 'Build or Test Failed.'
        }
    }
}
