pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
    }

    environment {
        IMAGE_NAME = "banking-app"
        IMAGE_TAG  = "build-${BUILD_NUMBER}"
        ACTIVE     = ""
        TARGET     = ""
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

                    def detectedActive = sh(
                        script: './deployment/scripts/detect-active.sh',
                        returnStdout: true
                    ).trim()

                    if (detectedActive != 'blue' &&
                        detectedActive != 'green') {

                        error(
                            "Unable to determine active environment. " +
                            "detect-active.sh returned: '${detectedActive}'"
                        )
                    }

                    env.ACTIVE = detectedActive

                    echo "====================================="
                    echo "Current Active Environment : ${env.ACTIVE}"
                    echo "====================================="
                }
            }
        }

        stage('Choose Target') {
            steps {

                script {

                    if (env.ACTIVE == 'blue') {

                        env.TARGET = 'green'

                    } else if (env.ACTIVE == 'green') {

                        env.TARGET = 'blue'

                    } else {

                        error(
                            "Invalid active environment: ${env.ACTIVE}"
                        )
                    }

                    echo "====================================="
                    echo "Active Environment : ${env.ACTIVE}"
                    echo "Deployment Target  : ${env.TARGET}"
                    echo "Image              : ${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                    echo "====================================="
                }
            }
        }

        stage('Deploy Target') {
            steps {

                sh """
                    echo "Deploying ${IMAGE_NAME}:${IMAGE_TAG} to banking-${TARGET}"

                    echo "Removing previous banking-${TARGET} container..."

                    docker rm -f banking-${TARGET} || true

                    echo "Starting banking-${TARGET}..."

                    BANKING_IMAGE=${IMAGE_NAME}:${IMAGE_TAG} \
                    docker compose -p banking-app up -d \
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
                    echo "Switching traffic to ${TARGET}"

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

                    echo ""
                    echo "Smoke Test PASSED"
                '''
            }
        }
    }

    post {

        success {

            echo "====================================="
            echo "Blue/Green Deployment Successful"
            echo "====================================="
            echo "Previous Environment : ${env.ACTIVE}"
            echo "Live Environment     : ${env.TARGET}"
            echo "Image                : ${env.IMAGE_NAME}:${env.IMAGE_TAG}"
            echo "====================================="
        }

        failure {

            script {

                echo "====================================="
                echo "Pipeline Failed"
                echo "====================================="

                if (env.ACTIVE?.trim() &&
                    (env.ACTIVE == 'blue' ||
                     env.ACTIVE == 'green')) {

                    echo "Rolling back to ${env.ACTIVE}"

                    if (fileExists(
                        'deployment/scripts/rollback.sh'
                    )) {

                        sh """
                            chmod +x deployment/scripts/rollback.sh

                            ./deployment/scripts/rollback.sh \
                                ${env.ACTIVE}
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
