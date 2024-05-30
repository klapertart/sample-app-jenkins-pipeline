pipeline{
    agent any

    stages{
        stage('Hello'){
            steps{
                echo 'Hello world'
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