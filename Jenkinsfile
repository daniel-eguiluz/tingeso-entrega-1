pipeline {
    agent any
    tools {
        maven "maven"
    }
    stages {
        stage("Build Frontend") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/daniel-eguiluz/tingeso-entrega-1.git']])
                dir("payroll-frontend") {
                    bat "npm install"
                    bat "npm run build"
                }
            }
        }
        stage("Build Frontend Docker Image and Push") {
            steps {
                dir("payroll-frontend") {
                    script {
                        withDockerRegistry(credentialsId: 'docker-credentials') {
                            bat "docker build -t danieleguiluz44/payroll-frontend ."
                            bat "docker push danieleguiluz44/payroll-frontend"
                        }
                    }
                }
            }
        }
        stage("Build JAR File") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/daniel-eguiluz/tingeso-entrega-1.git']])
                dir("payroll-backend") {
                    bat "mvn clean install"
                }
            }
        }
        stage("Test Backend") {
            steps {
                dir("payroll-backend") {
                    bat "mvn test"
                }
            }
        }
        stage("Build Backend Docker Image and Push") {
            steps {
                dir("payroll-backend") {
                    script {
                        withDockerRegistry(credentialsId: 'docker-credentials') {
                            bat "docker build -t danieleguiluz44/payroll-backend ."
                            bat "docker push danieleguiluz44/payroll-backend"
                        }
                    }
                }
            }
        }
    }
}
