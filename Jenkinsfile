pipeline {
    agent any

    environment {
        REGISTRY = 'harbor.proxbenovh.cloud'
        HARBOR_PROJECT = 'devops-project-harbor'
        IMAGE_NAME = 'accenture-interview-app'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                script {
                    env.GIT_SHA = sh(
                        script: 'git rev-parse HEAD',
                        returnStdout: true
                    ).trim()

                    env.IMAGE = "${REGISTRY}/${HARBOR_PROJECT}/${IMAGE_NAME}:${GIT_SHA}"
                }

                sh '''
                    echo "Commit: $GIT_SHA"
                    echo "Image:  $IMAGE"
                '''
            }
        }

        stage('Test') {
            steps {
                sh '''
                    docker run --rm \
                      -v "$WORKSPACE/app:/workspace" \
                      -w /workspace \
                      maven:3.9-eclipse-temurin-21 \
                      mvn -B test
                '''
            }
        }

        stage('Build Image') {
            steps {
                sh '''
                    docker build \
                      -t "$IMAGE" \
                      app/
                '''
            }
        }

        stage('Push Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'harbor-robot-devops-project-harbor',
                        usernameVariable: 'HARBOR_USER',
                        passwordVariable: 'HARBOR_PASSWORD'
                    )
                ]) {
                    sh '''
                        set -eu

                        export DOCKER_CONFIG="$WORKSPACE/.docker-${BUILD_NUMBER}"
                        mkdir -p "$DOCKER_CONFIG"

                        printf '%s' "$HARBOR_PASSWORD" | \
                          docker login "$REGISTRY" \
                          -u "$HARBOR_USER" \
                          --password-stdin

                        docker push "$IMAGE"

                        rm -rf "$DOCKER_CONFIG"
                    '''
                }
            }
        }
    }

    post {
        always {
            sh '''
                docker image rm "$IMAGE" 2>/dev/null || true
                rm -rf "$WORKSPACE/.docker-${BUILD_NUMBER}" 2>/dev/null || true
            '''
        }
    }
}
