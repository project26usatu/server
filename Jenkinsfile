pipeline {
    agent any

    environment {
        PROJECT_PATH = sh(script: 'pwd', , returnStdout: true).trim()
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cleaning up workspace for project...'
                cleanWs()
                echo 'Checking out project from GitHub repository...'
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: '*/main']], 
                    userRemoteConfigs: [[url: 'https://github.com/abramov26/project26-server.git']]
                ])
            }
        }
        stage('Build'){
            steps {
                echo 'Building project...'
                withMaven(maven : 'apache-maven-3.8.1') {
                    sh 'mvn -DskipTests=true clean package'
                }
            }
        }
        stage('Test'){
            steps {
                echo 'Running Tests...'
                withMaven(maven : 'apache-maven-3.8.1') {
                    sh 'mvn test'
                }
            }
        }
        stage('Deploy'){
            steps {
                echo 'Copying files...'
                sh '''
                    yes | cp -rf $PROJECT_PATH/output/api.war /srv/jetty/wars
                    yes | cp -rf $PROJECT_PATH/www/* /srv/nginx/coursework
                '''
                echo 'Restarting Jetty...'
                sh 'docker restart project26_jetty'
            }
        }         
    }
}
