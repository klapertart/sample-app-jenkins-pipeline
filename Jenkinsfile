pipeline{
    agent any

    stages{
        stage('Build'){
            steps{
                echo 'start build'
                sh("./mvnw clean compile test-compile")
            }
        }
        stage('Test'){
            steps{
                echo 'stage test'
            }
        }
        stage('Deploy'){
            steps{
                echo 'stage deploy'
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