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
                        docker build \
                        -t ${IMAGE_NAME}:${IMAGE_TAG} \
                        -f deployment/docker/Dockerfile .
                    """

                    echo "Built image ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Detect Active') {
            steps {

                script {

                    ACTIVE = sh(
                        script: "./deployment/scripts/detect-active.sh",
                        returnStdout: true
                    ).trim()

                    echo "Current Active Environment : ${ACTIVE}"

                }
            }
        }

        stage('Choose Target') {
            steps {

                script {

                    if (ACTIVE == "blue") {

                        TARGET = "green"

                    } else {

                        TARGET = "blue"

                    }

                    echo "Deploying To : ${TARGET}"

                }

            }
        }

        stage('Deploy Target') {

            steps {

                sh """
                    echo "Deploying ${IMAGE_NAME}:${IMAGE_TAG} to banking-${TARGET}"

                    BANKING_IMAGE=${IMAGE_NAME}:${IMAGE_TAG} \
                    docker compose up -d \
                    --no-deps \
                    --force-recreate \
                    banking-${TARGET}
                """

            }

        }

        stage('Health Check') {

            steps {

                sh """
                    chmod +x deployment/scripts/health-check.sh
                    ./deployment/scripts/health-check.sh ${TARGET}
                """

            }

        }

        stage('Switch Traffic') {

            steps {

                sh """
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
                '''

            }

        }

    }

    post {

        success {

            echo "====================================="
            echo "Blue/Green Deployment Successful"
            echo "Live Environment : ${TARGET}"
            echo "====================================="

        }

        failure {

            script {

                echo "Pipeline Failed"

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
