pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Unit Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQube') {
            withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                sh '''
                    mvn sonar:sonar \
                    -Dsonar.projectKey=enterprise-banking-platform \
                    -Dsonar.projectName=EnterpriseBanking \
                    -Dsonar.token=$SONAR_TOKEN
                '''
            }
        }
    }
}

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build \
                -t banking-app:${BUILD_NUMBER} \
                -f deployment/docker/Dockerfile .
                '''
            }
        }

    }

    post {

        success {

            echo 'Pipeline Completed Successfully'

        }

        failure {

            echo 'Pipeline Failed'

        }

    }

}
