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
                echo "Building Docker image: ${BUILD_NUMBER}"
                bat "docker build -t manjugowda200523/devops-cicd-demo:${BUILD_NUMBER} ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    bat 'docker login -u "%DOCKER_USERNAME%" -p "%DOCKER_PASSWORD%"'
                    bat "docker push manjugowda200523/devops-cicd-demo:${BUILD_NUMBER}"
                }
            }
        }
        stage('Update Manifest Repo') {
            steps {
                echo "Updating Kubernetes manifest to image ${BUILD_NUMBER}"

                dir('manifests') {
                    git branch: 'main',
                        credentialsId: 'github-manifest-credentials',
                        url: 'https://github.com/manjugowda-l/devops-cicd-manifests.git'

                    bat '''
                        powershell -Command "(Get-Content deployment.yaml) -replace 'image: manjugowda200523/devops-cicd-demo:[0-9]+', 'image: manjugowda200523/devops-cicd-demo:%BUILD_NUMBER%' | Set-Content deployment.yaml"
                    '''

                    bat 'git config user.name "Jenkins"'
                    bat 'git config user.email "jenkins@local"'

                    bat 'git add deployment.yaml'
                    bat 'git commit -m "Update image to build %BUILD_NUMBER%"'

                    withCredentials([gitUsernamePassword(
                        credentialsId: 'github-manifest-credentials',
                        gitToolName: 'Default'
                    )]) {
                        bat 'git push origin main'
                    }
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