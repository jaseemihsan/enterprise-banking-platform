def activeEnvironment = ''
def targetEnvironment = ''

pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
    }

    environment {
        IMAGE_NAME = "banking-app"
        IMAGE_TAG  = "build-${BUILD_NUMBER}"
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

                echo "Building Docker image ${IMAGE_NAME}:${IMAGE_TAG}"

                sh """
                    docker build \
                      -t ${IMAGE_NAME}:${IMAGE_TAG} \
                      -f deployment/docker/Dockerfile .
                """

                echo "Built image ${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }

        stage('Detect Active') {
            steps {
                script {

                    activeEnvironment = sh(
                        script: './deployment/scripts/detect-active.sh',
                        returnStdout: true
                    ).trim()

                    echo "====================================="
                    echo "Current Active Environment : ${activeEnvironment}"
                    echo "====================================="

                    if (activeEnvironment != 'blue' &&
                        activeEnvironment != 'green') {

                        error(
                            "Invalid active environment returned: '${activeEnvironment}'"
                        )
                    }
                }
            }
        }

        stage('Choose Target') {
            steps {
                script {

                    if (activeEnvironment == 'blue') {

                        targetEnvironment = 'green'

                    } else if (activeEnvironment == 'green') {

                        targetEnvironment = 'blue'

                    } else {

                        error(
                            "Invalid active environment: ${activeEnvironment}"
                        )
                    }

                    echo "====================================="
                    echo "Active Environment : ${activeEnvironment}"
                    echo "Deployment Target  : ${targetEnvironment}"
                    echo "Image              : ${IMAGE_NAME}:${IMAGE_TAG}"
                    echo "====================================="
                }
            }
        }

        stage('Deploy Target') {
            steps {

                sh """
                    echo "====================================="
                    echo "Deploying ${IMAGE_NAME}:${IMAGE_TAG}"
                    echo "Target: banking-${targetEnvironment}"
                    echo "====================================="

                    echo "Removing previous banking-${targetEnvironment} container..."

                    docker rm -f banking-${targetEnvironment} || true

                    echo "Starting banking-${targetEnvironment}..."

                    BANKING_IMAGE=${IMAGE_NAME}:${IMAGE_TAG} \
                    docker compose -p banking-app up -d \
                      --no-deps \
                      banking-${targetEnvironment}

                    echo "Deployment container started"
                """
            }
        }

        stage('Health Check') {
            steps {

                sh """
                    echo "====================================="
                    echo "Running health check for ${targetEnvironment}"
                    echo "====================================="

                    chmod +x deployment/scripts/health-check.sh

                    ./deployment/scripts/health-check.sh ${targetEnvironment}
                """
            }
        }

        stage('Switch Traffic') {
            steps {

                sh """
                    echo "====================================="
                    echo "Switching traffic to ${targetEnvironment}"
                    echo "====================================="

                    chmod +x deployment/scripts/switch-traffic.sh

                    ./deployment/scripts/switch-traffic.sh ${targetEnvironment}
                """
            }
        }

        stage('Smoke Test') {
            steps {

                sh '''
                    echo "====================================="
                    echo "Running Smoke Test"
                    echo "====================================="

                    curl -f http://localhost/

                    echo ""
                    echo "Smoke Test PASSED"
                '''
            }
        }
    }

    post {

        success {

            echo "====================================="
            echo "BLUE/GREEN DEPLOYMENT SUCCESSFUL"
            echo "====================================="
            echo "Previous Environment : ${activeEnvironment}"
            echo "Live Environment     : ${targetEnvironment}"
            echo "Image                : ${IMAGE_NAME}:${IMAGE_TAG}"
            echo "====================================="
        }

        failure {

            script {

                echo "====================================="
                echo "PIPELINE FAILED"
                echo "====================================="

                if (activeEnvironment == 'blue' ||
                    activeEnvironment == 'green') {

                    echo "Rolling back to ${activeEnvironment}"

                    if (fileExists(
                        'deployment/scripts/rollback.sh'
                    )) {

                        sh """
                            chmod +x deployment/scripts/rollback.sh

                            ./deployment/scripts/rollback.sh \
                                ${activeEnvironment}
                        """

                    } else {

                        echo "rollback.sh not found."
                        echo "Rollback skipped."
                    }

                } else {

                    echo "Active environment is unknown."
                    echo "Rollback skipped."
                }
            }
        }

        always {

            echo "Pipeline completed."

        }
    }
}
