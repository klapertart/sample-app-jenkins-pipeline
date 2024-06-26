pipeline {
    agent any

    environment {
            GITHUB_TOKEN = credentials('github-token')
            IMAGE_NAME = "app-pipeline-multibranch"
            DOCKER_REGISTRY_URL = 'localhost:80'
            }

    options {
        // Set log rotation to keep the last 10 builds and discard older ones
        buildDiscarder(logRotator(numToKeepStr: '1', daysToKeepStr: '1'))
    }

    tools {
        // Ensure this matches the Maven tool name configured in Jenkins
        maven '3.6.3'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: "https://klapertart:${GITHUB_TOKEN}@github.com/klapertart/sample-app-jenkins-pipeline.git", branch: 'master'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Check for Changes') {
            steps {
                script {
                    // Execute git diff command to check for changes
                    def gitChanges = sh(returnStdout: true, script: 'git diff --exit-code')
                    if (gitChanges.trim().isEmpty()) {
                        echo "No changes in the working directory."
                    } else {
                        echo "Changes detected in the working directory."
                        echo "Changes:\n${gitChanges}"
                        sh 'git add .'
                        sh 'git commit -m "update"'
                    }
                }

            }
        }
        stage('Prepare Release') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'github-token', variable: 'GITHUB_TOKEN')]) {
                        sh '''
                            mvn release:clean release:prepare -Dusername=klapertart -Dpassword=${GITHUB_TOKEN}
                        '''
                    }
                }
            }
        }
        stage('Perform Release') {
            steps {
                sh 'mvn release:perform'
            }
        }
        stage('Genereate Changelog') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'github-token', variable: 'GITHUB_TOKEN')]) {
                        sh '''
                            mvn generate-resources
                            git add .
                            git commit -m "docs: update changelog"
                            git push origin master
                        '''
                    }
                }
            }
        }
        stage('Get Git Tag') {
            steps {
                script {
                    // Get the latest Git tag
                    env.GIT_TAG = sh(script: "git describe --tags --abbrev=0", returnStdout: true).trim()
                    // Alternatively, to get the most recent tag reachable from the current commit:
                    // env.GIT_TAG = sh(script: "git describe --tags --always", returnStdout: true).trim()
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${env.IMAGE_NAME}:${env.GIT_TAG}")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {                        // Log in to Docker Hub
                        sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin ${env.DOCKER_REGISTRY_URL}"

                        // Tag the Docker image for the local registry
                        sh "docker tag ${env.IMAGE_NAME}:${env.GIT_TAG} ${env.DOCKER_REGISTRY_URL}/${env.IMAGE_NAME}:${env.GIT_TAG}"

                        // Push the Docker image to the local registry
                        sh "docker push ${env.DOCKER_REGISTRY_URL}/${env.IMAGE_NAME}:${env.GIT_TAG}"

                        // Log out from the local Docker registry
                        sh "docker logout ${env.DOCKER_REGISTRY_URL}"
                    }
                }
            }
        }
    }
}