pipeline {
    agent any

    environment {
            GITHUB_TOKEN = credentials('github-token')
            IMAGE_NAME = "${env.JOB_NAME}"
            TAG = "${env.TAG_NAME}"
            }

    tools {
        maven '3.6.3' // Ensure this matches the Maven tool name configured in Jenkins
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
    }

   post {
        always {
            // Clean up unused Docker images to save space
            script {
                sh 'docker system prune -af'
            }
        }
    }
}