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
                    bat '.\\mvnw.cmd org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=devops-cicd-demo'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                bat 'docker build -t devops-cicd-demo:1.0 .'
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