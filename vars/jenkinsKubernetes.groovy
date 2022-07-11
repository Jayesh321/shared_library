def call ( String repo = 'a', String branch = 'a', String git_cred = 'a')

pipeline{
    environment{
        Git_Repo = "${repo}"
        Git_Branch = "${branch}"
        GIt_Cred = "${git_cred}"
    }

    agent none
    
    triggers {
        pollSCM '* * * * *'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }

    stages{
        stage(SCM){
            agent{label 'docker_slave'}
            steps{
                checkout([$class: 'GitSCM', branches: [[name: "$Git_Branch"]], extensions: [], userRemoteConfigs: [[credentialsId: "$GIt_Cred", url: "$Git_Repo"]]])
            }

        }
    }

    


}




// def call ( String dockerCred = 'a', String dockerRepo = 'a', String docTag = 'a', String grepo = 'a', String gbranch = 'a', String gitcred = 'a'  ) {

// pipeline {
//     environment {
//         dockerCredential = "${dockerCred}"
//         dockerRepository = "${dockerRepo}"
//         dockerTag = "${docTag}_$BUILD_NUMBER"
//         gitRepo = "${grepo}"
//         gitBranch = "${gbranch}"
//         gitCred = "${gitcred}"
//     }

//     agent none

//     triggers {
//         pollSCM '* * * * * '
//     }

//     options {
//     buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
//     }

//     stages {
//         stage('POLL SCM') {
//             agent{label 'docker_slave'}
//             steps {
//                 checkout([$class: 'GitSCM', branches: [[name: "$gitBranch"]], extensions: [], userRemoteConfigs: [[credentialsId: "$gitCred", url: "$gitRepo"]]])
//             }
//         }

//         stage('SonarQube analysis') {
//             agent{label 'docker_slave'}
//             steps { 
//                 script{
//                     //def sonarScanner = tool name: 'sonarqube', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
//                     def scannerHome = tool 'sonarqube';
//                     withSonarQubeEnv('Sonar_Server') {
//                         sh "${tool("scannerHome")}/bin/sonar-scanner \
//                         -D sonar.projectKey=jenkins \
//                         -D sonar.projectName=jenkins \
//                         -D sonar.host.url=http://52.66.115.34:9000/ \
//                         -D sonar.sources=/var/lib/jenkins/workspace/$JOB_NAME"
//                     }
//                 }
//             }
//         }

//         //stage('BUILD STAGE') {
//         //     agent{label 'docker_slave'}
//         //     steps {
//         //         script {
//         //             //dockerimage = docker.build("'$dockerRepository':$dockerTag")
//         //             dockerimage = dockerImage = docker.build dockerRepository + ":$dockerTag" 

//         //         }
//         //     }
//         // }

//         // stage('PUSH TO DOCKER HUB') {
//         //     agent{label 'docker_slave'}
//         //     steps { 
//         //         script {
//         //             docker.withRegistry('', "$dockerCredential") {
//         //                 dockerImage.push()
//         //             }
//         //             sh 'docker image rmi jayesh313/k8s:"$dockerTag"'
//         //         }
//         //     }
//         // } 

//         // stage ('Deploy') {
//         //     agent{label 'k8s_slave'}
//         //     steps {
//         //         sh 'kubectl set image deployment/webapp-deployment nodejs="$dockerRepository:$dockerTag" --record'
//         //     }
//         // }
//     }

// }

// }
