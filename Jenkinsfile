pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
    }

    environment {
        IMAGE_NAME = "banking-app"
        IMAGE_TAG = "build-${BUILD_NUMBER}"
        ACTIVE = ""
        TARGET = ""
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
                    withCredentials([
                        string(
                            credentialsId: 'sonarqube-token',
                            variable: 'SONAR_TOKEN'
                        )
                    ]) {

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
                sh """
                    echo "Building Docker image ${IMAGE_NAME}:${IMAGE_TAG}"

                    docker build \
                    -t ${IMAGE_NAME}:${IMAGE_TAG} \
                    -f deployment/docker/Dockerfile .

                    echo "Built image ${IMAGE_NAME}:${IMAGE_TAG}"
                """
            }
        }

        stage('Detect Active') {
            steps {
                script {

                    ACTIVE = sh(
                        script: "./deployment/scripts/detect-active.sh",
                        returnStdout: true
                    ).trim()

                    echo "====================================="
                    echo "Current Active Environment : ${ACTIVE}"
                    echo "====================================="
                }
            }
        }

        stage('Choose Target') {
            steps {
                script {

                    if (ACTIVE == "blue") {

                        TARGET = "green"

                    } else if (ACTIVE == "green") {

                        TARGET = "blue"

                    } else {

                        error("Unable to determine active environment: ${ACTIVE}")

                    }

                    echo "====================================="
                    echo "Deploying To : ${TARGET}"
                    echo "Docker Image : ${IMAGE_NAME}:${IMAGE_TAG}"
                    echo "====================================="
                }
            }
        }

        stage('Deploy Target') {
            steps {
                sh """
                    echo "====================================="
                    echo "Deploying ${IMAGE_NAME}:${IMAGE_TAG}"
                    echo "Target : banking-${TARGET}"
                    echo "====================================="

                    echo "Removing previous ${TARGET} container..."

                    docker rm -f banking-${TARGET} || true

                    echo "Starting ${TARGET} with image ${IMAGE_NAME}:${IMAGE_TAG}"

                    BANKING_IMAGE=${IMAGE_NAME}:${IMAGE_TAG} \
                    docker compose up -d \
                    --no-deps \
                    banking-${TARGET}

                    echo "Deployment container started"
                """
            }
        }

        stage('Health Check') {
            steps {
                sh """
                    echo "Running health check for ${TARGET}"

                    chmod +x deployment/scripts/health-check.sh

                    ./deployment/scripts/health-check.sh ${TARGET}
                """
            }
        }

        stage('Switch Traffic') {
            steps {
                sh """
                    echo "Switching production traffic to ${TARGET}"

                    chmod +x deployment/scripts/switch-traffic.sh

                    ./deployment/scripts/switch-traffic.sh ${TARGET}
                """
            }
        }

        stage('Smoke Test') {
            steps {
                sh '''
                    echo "Running Smoke Test..."

                    curl -f http://localhost/

                    echo "Smoke Test Passed"
                '''
            }
        }
    }

    post {

        success {
            echo "====================================="
            echo "Blue/Green Deployment Successful"
            echo "Live Environment : ${TARGET}"
            echo "Docker Image     : ${IMAGE_NAME}:${IMAGE_TAG}"
            echo "====================================="
        }

        failure {
            script {

                echo "====================================="
                echo "Pipeline Failed"
                echo "====================================="

                if (ACTIVE?.trim()) {

                    echo "Rolling back to ${ACTIVE}"

                    sh """
                        chmod +x deployment/scripts/rollback.sh

                        ./deployment/scripts/rollback.sh ${ACTIVE}
                    """

                } else {

                    echo "Deployment never started. Rollback skipped."

                }
            }
        }

        always {
            cleanWs()
        }
    }
}
