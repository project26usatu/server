pipeline {
    agent any

    environment {
        PROJECT_PATH = sh(script: 'pwd', , returnStdout: true).trim()
    }

    stages {
        stage('Build'){
            steps {
                echo 'Building Project...'
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
                    yes | cp -rf $PROJECT_PATH/www/* /srv/nginx
                '''
                echo 'Restarting Jetty...'
                sh 'docker restart project26_jetty'
            }
        }         
    }
}
