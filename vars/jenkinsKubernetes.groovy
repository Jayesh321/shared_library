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

    stages {
        stage('POLL SCM') {
            agent{label 'docker_slave'}
            steps {
                checkout([$class: 'GitSCM', branches: [[name: "$gitBranch"]], extensions: [], userRemoteConfigs: [[credentialsId: "$gitCred", url: "$gitRepo"]]])
            }
        }

        stage('BUILD STAGE') {
            agent{label 'docker_slave'}
            steps {
                script {
                    dockerimage = docker.build("'$dockerRepository':$dockerTag")
                }
            }
        }


    }

}

}
