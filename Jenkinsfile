pipeline {
    agent any

    environment {
            GIT_USER_NAME = 'klapertart'
            GIT_USER_EMAIL = 'klapertartinc@gmail.com'
            GITHUB_TOKEN = credentials('github-token')
            GIT_BRANCH = 'master'
            GIT_URL = 'https://github.com/klapertart/sample-app-jenkins-pipeline.git'
            DOCKER_IMAGE_NAME = "app-pipeline-multibranch"
            DOCKER_REGISTRY_URL = 'localhost:80'
            APP_VERSION = '-DEV'
            }

    triggers {
        pollSCM('H/5 * * * *') // Poll the SCM every 5 minutes
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
                echo "Branch to checkout: ${GIT_BRANCH}"
                git url: "https://klapertart:${GITHUB_TOKEN}@github.com/klapertart/sample-app-jenkins-pipeline.git", branch: "${GIT_BRANCH}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Configure Git User'){
            steps {
                // Configure Git user name and email (local)
                sh "git config user.name '${GIT_USER_NAME}'"
                sh "git config user.email '${GIT_USER_EMAIL}'"
            }
        }

        stage('Check for Changes') {
            steps {
                script {
                    // Run the git diff command and capture the exit code
                    def gitStatus = sh(returnStatus: true, script: 'git diff --exit-code > diff.txt')
                    def gitChanges = readFile('diff.txt').trim()

                    // Check the exit code to determine if there are changes
                    if (gitStatus == 0) {
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

        stage('Get Project Version') {
            steps {
                script {
                    def projectVersion = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                    echo "Project version: ${projectVersion}"
                    def splitVersion = projectVersion.split('-')
                    env.TAG_TO_CHECK = splitVersion[0] + env.APP_VERSION
                }
            }
        }

        stage('Check Tag Existence') {
            steps {
                script {
                    sh 'git tag -l'
                    def tagExists = sh(script: "git tag -l ${TAG_TO_CHECK}", returnStdout: true).trim()
                    if (tagExists) {
                        echo "Tag ${TAG_TO_CHECK} exists."
                        // You can set an environment variable or take other actions here
                        env.TAG_EXISTS = 'true'
                    } else {
                        echo "Tag ${TAG_TO_CHECK} does not exist."
                        env.TAG_EXISTS = 'false'
                    }
                }
            }
        }

        stage('Prepare Release') {
            when {
                expression { env.TAG_EXISTS == 'false' }
            }
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
            when {
                expression { env.TAG_EXISTS == 'false' }
            }
            steps {
                sh 'mvn release:perform'
            }
        }

        stage('Genereate Changelog') {
            when {
                expression { env.TAG_EXISTS == 'false' }
            }
            steps {
                script {
                    withCredentials([string(credentialsId: 'github-token', variable: 'GITHUB_TOKEN')]) {
                        sh '''
                            mvn generate-resources
                            git add .
                            git commit -m "docs: update changelog"
                            git push origin ${GIT_BRANCH}
                        '''
                    }
                }
            }
        }

        stage('Get Git Tag') {
            when {
                expression { env.TAG_EXISTS == 'false' }
            }
            steps {
                script {
                    // Get the latest Git tag
                    env.GIT_TAG = sh(script: "git describe --tags --abbrev=0", returnStdout: true).trim()
                }
            }
        }

        stage('Build Docker Image') {
            when {
                expression { env.TAG_EXISTS == 'false' }
            }
            steps{
                script {
                    docker.withRegistry("${DOCKER_REGISTRY_URL}", "dockerhub-credentials") {
                        docker.build("${DOCKER_IMAGE_NAME}:${GIT_TAG}").push()
                    }
                }
            }
        }

        stage('Cleanup Unused Docker Images') {
            when {
                expression { env.TAG_EXISTS == 'false' }
            }
            steps {
                script {
                    sh 'docker image prune -f'
                }
            }
        }
    }
}