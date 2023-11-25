pipeline {
    agent {
            kubernetes {
                label 'k8s'
                defaultContainer 'jnlp'
                yaml """
apiVersion: v1
kind: Pod
metadata:
labels:
    app: my-build-app
spec:
    containers:
     - name: maven
       image: maven:3.6.3-jdk-11
       command:
        - cat
       tty: true
     - name: docker
       image: docker
       volumeMounts:
        - name: docker-sock
          mountPath: /var/run/docker.sock
    volumes:
      - name: docker-sock
        hostPath:
          path: /var/run/docker.sock
                """
            }
      }

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
                container('maven') {
                    echo 'Building and Testing...'
                    echo "The Jenkins job name is: ${env.JOB_NAME}"
                    sh 'mvn clean install'
                }
                container('docker') {
                    dir('microblogging-service/Kafka-consumer-service') {
                        echo 'Building and Testing...'
                        sh 'docker build -t moukthikavuyyuru/kafka-consumer-service:latest .'
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