def call ( String dockerCred = 'a', String dockerRepo = 'a', String docTag = 'a', String grepo = 'a', String gbranch = 'a', String gitcred = 'a'  ) {

pipeline {
    environment {
        dockerCredential = "${dockerCred}"
        dockerRepository = "${dockerRepo}"
        dockerTag = "${docTag}_$BUILD_NUMBER"
        gitRepo = "${grepo}"
        gitBranch = "${gbranch}"
        gitCred = "${gitcred}"
    }

    agent none

    triggers {
        pollSCM '* * * * * '
    }

    options {
    buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }

    stages {
        stage('POLL SCM') {
            agent{label 'docker_slave'}
            steps {
                checkout([$class: 'GitSCM', branches: [[name: "$gitBranch"]], extensions: [], userRemoteConfigs: [[credentialsId: "$gitCred", url: "$gitRepo"]]])
            }
        }

        stage('SonarQube analysis') {
            agent{label 'docker_slave'}
            steps {
                script{
                    def sonarScanner = tool name: 'SonarQube', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    //def scannerHome = tool 'sonarqube';
                    withSonarQubeEnv('Sonar_Server') {
                        sh "${tool("scannerHome")}/bin/sonar-scanner \
                        -Dsonar.projectKey=jenkins \
                        -Dsonar.projectName=jenkins" \
                        -Dsonar.host.url=http://52.66.115.34:9000/"
                    }
                }
            }
        }

        // stage('BUILD STAGE') {
        //     agent{label 'docker_slave'}
        //     steps {
        //         script {
        //             //dockerimage = docker.build("'$dockerRepository':$dockerTag")
        //             dockerimage = dockerImage = docker.build dockerRepository + ":$dockerTag" 

        //         }
        //     }
        // }

        // stage('PUSH TO DOCKER HUB') {
        //     agent{label 'docker_slave'}
        //     steps { 
        //         script {
        //             docker.withRegistry('', "$dockerCredential") {
        //                 dockerImage.push()
        //             }
        //             sh 'docker image rmi jayesh313/k8s:"$dockerTag"'
        //         }
        //     }
        // } 

        // stage ('Deploy') {
        //     agent{label 'k8s_slave'}
        //     steps {
        //         sh 'kubectl set image deployment/webapp-deployment nodejs="$dockerRepository:$dockerTag" --record'
        //     }
        // }
    }

}

}
