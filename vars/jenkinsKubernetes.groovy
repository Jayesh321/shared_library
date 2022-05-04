def call ( String grepo = 'a', String gbranch = 'a', String gitcred = 'a'  ) {

pipeline {
    environment {
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

    }

}

}