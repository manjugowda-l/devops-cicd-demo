pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                echo 'Starting Maven build...'
                bat '.\\mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                bat '.\\mvnw.cmd test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat '.\\mvnw.cmd sonar:sonar -Dsonar.projectKey=devops-cicd-demo'
                }
            }
        }

    }

    post {
        success {
            echo 'CI Pipeline completed successfully!'
        }

        failure {
            echo 'CI Pipeline failed!'
        }
    }
}