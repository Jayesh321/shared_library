def call ( String "grepo" = 'a', String "gbranch" = 'a', String "gitcred" = 'a'  )

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
            agent{label 'docker'}
            steps {
                checkout([$class: 'GitSCM', branches: [[name: "$gitBranch"]], extensions: [], userRemoteConfigs: [[credentialsId: "$gitCred", url: "$gitRepo"]]])
            }
        }

        stage('BUILD STAGE') {
            agent{label 'docker'}
            steps {
                // One or more steps need to be included within the steps block.
            }
        }

        stage('PUSH TO DOCKER HUB') {
            agent{label 'docker'}
            steps {
                // One or more steps need to be included within the steps block.
            }
        } 

        stage('DEPLOY') {
            steps {
                // One or more steps need to be included within the steps block.
            }
        } 

    }

}