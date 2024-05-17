pipeline {
    agent any
    tools {
        maven 'jenkins-maven'
    }

    stages {
        stage('Git Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extension, [], userRemoteConfigs: [[url: 'https://github.com/nurdinahmadalawiyah/simple-crud-api-spring-boot.git']])
                sh 'mvn clean install'
                echo 'Git Checkout Completed'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn clean package'
                    sh '''mvn clean verify sonar:sonar -Dsonar.projectKey=crud-spring-boot -Dsonar.projectName='crud-spring-boot' -Dsonar.host.url=http://localhost:9000 '''
                    echo 'SonarQube Analysis Completed'
                }
            }
        }
        stage('Quality Gate') {
            steps {
                waitForQualityGate abortPipeline: true
                echo 'Quality Gate Completed'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t simple-crud-api-spring-boot:0.0.1 .'
                    echo 'Build Docker Image Completed'
                }
            }
        }
        stage('Docker Push') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhub-pwd'. variable: 'dockerhub-password')]) {
                        sh ''' docker login -u nurdincode -p "%dockerhub-password" '''
                    }
                    sh 'docker push simple-crud-api-spring-boot:0.0.1'
                }
            }
        }
        stage('Docker Run') {
            steps {
                script {
                    sh 'docker run -d --name simple-crud-api-spring-boot -p 8090:8000 simple-crud-api-spring-boot:0.0.1'
                    echo 'Docker Run Completed'
                }
            }
        }
    }
    post {
        always {
            bat 'docker logout'
        }
    }
}