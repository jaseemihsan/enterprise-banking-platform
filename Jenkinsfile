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

        stage('Docker Build') {
    steps {
        script {
            IMAGE_NAME = "banking-app"
            IMAGE_TAG = "build-${BUILD_NUMBER}"

            sh """
            docker build \
            -t ${IMAGE_NAME}:${IMAGE_TAG} \
            -f deployment/docker/Dockerfile .
            """
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
