pipeline {
    agent any

    environment {
            DOCKER_USERNAME = ''
            DOCKER_PASSWORD = ''
            GITHUB_TOKEN = credentials('github-token')
            IMAGE_NAME = "${env.JOB_NAME}"
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
                    withEnv(['DOCKER_USERNAME', 'DOCKER_PASSWORD']) {
                        // Log in to Docker Hub
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'

                        // Push the Docker image to the registry
                         sh "docker push ${env.IMAGE_NAME}:${env.GIT_TAG}"

                        // Log out from Docker Hub
                        sh 'docker logout'
                    }
                }
            }
        }

    }
}