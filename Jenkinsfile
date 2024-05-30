pipeline{
    agent any

    stages{
        stage('Build'){
            steps{
                echo 'stage build'
            }
        }
        stage('Test'){
            steps{
                echo 'stage test'
            }
        }
        stage('Build'){
            steps{
                echo 'stage build'
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