pipeline{
    agent any
    environment{
        AUTHOR = "Otong Sunandar"
        APP = credentials("otong123")
    }

    triggers{
        cron("* */1 * * *")
        //pollSCM("*/5 * * * *")
    }

    parameters{
        string(name: "NAME", defaultValue: "Guest", description: "What is your name")
        text(name: "DESCRIPTION", defaultValue: "Guest", description: "Tell me about you")
        booleanParam(name: "DEPLOY", defaultValue: "false", description: "Need to Deploy?")
        choice(name: "SOCIAL_MEDIA", choices: ["Instagram", "Facebook", "Twitter"], description: "Which social media do youe have?")
        password(name: "SECRET", defaultValue: "", description: "Encrypt Key")
    }

    stages{
        stage("Params"){
            steps{
                echo "Hello: ${params.NAME}"
                echo "Description: ${params.DESCRIPTION}"
                echo "Deploy: ${params.DEPLOY}"
                echo "Social Media: ${params.SOCIAL_MEDIA}"
                echo "Secret: ${params.SECRET}"
            }
        }
        stage("Prepare"){
            steps{
                echo "Author: ${AUTHOR}"
                echo "App User: ${APP_USR}"
                echo "App Password: ${APP_PSW}"
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