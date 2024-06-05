pipeline{
    agent any
    environment{
        AUTHOR = "Otong Sunandar"
    }

    stages{
        stage("Prepare"){
            steps{
                echo "Author: ${AUTHOR}"
                echo "Start Job: ${env.JOB_NAME}"
                echo "Start Build: ${env.BUILD_NUMBER}"
                echo "Branch Name: ${env.BRANCH_NAME}"
            }
        }
        stage("Build"){
            steps{
                echo "start build"
                sh("chmod +x mvnw")
                sh("./mvnw clean compile test-compile")
            }
        }
        stage("Test"){
            steps{
                echo "stage test"
            }
        }
        stage("Deploy"){
            steps{
                echo "stage deploy"
            }
        }
    }

    post{
        always{
            echo "i will always run"
        }
        success{
            echo "yes, success"
        }
        failure{
            echo "oh no, failure"
        }
        cleanup{
            echo "dont care, success or failure"
        }
    }
}