pipeline {
    agent any

    tools{
        maven 'maven'
        git 'git'
        jdk 'jdk'
    }

    stages {
        stage('Build & Test') {
            steps {
                echo 'Building and Testing...'
                echo "The Jenkins job name is: ${env.JOB_NAME}"
                sh 'mvn clean install'
                sh 'mvn clean package'
            }
        }

        stage('Packing the App') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}