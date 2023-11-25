pipeline {
    agent any

    tools{
        maven 'maven'
        git 'git'
        jdk 'jdk'
    }
    environment {
        DOCKER_REGISTRY = 'moukthikavuyyuru'
        DOCKER_CREDENTIALS_ID = 'docker'
     }

    stages {
        stage('Build & Test') {
            steps {
                echo 'Building and Testing...'
                echo "The Jenkins job name is: ${env.JOB_NAME}"
                sh 'mvn clean install'
                sh 'mvn clean package'
                dir('MicroBlogging/Kafka-consumer-service') {
                    script {
                        withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                            sh "docker login -u $DOCKER_REGISTRY -p $PASSWORD"
                            sh "docker build -t ${DOCKER_REGISTRY}/kafka-consumer-service:latest ."
                            sh "docker push ${DOCKER_REGISTRY}/kafka-consumer-service:latest"
                        }
                    }
                }
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