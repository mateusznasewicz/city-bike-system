pipeline{
    agent none

    environment{
        REGISTRY = "192.168.56.200:5000"
        IMAGE_RE = "rental-service"
        IMAGE_PA = "payment-service"
        IMAGE_FL = "fleet-service"
        VERSION = "${BUILD_NUMBER}"
        SSH_CRED_ID = "vagrant-ssh-key"
        USER_EMAIL = "268513@student.pwr.edu.pl"
        MANAGER_IP = "192.168.56.200"
        WORKER_IP = "192.168.56.201"
    }

    stages {

        stage('Checkout Code') {
            agent any
            steps {
                script {
                    echo '---  Cloning Repository ---'
                    checkout scm
                }
            }
        }
        
        stage('Build (Local)'){
            agent {
                docker {
                    image 'docker:cli'
                    args '-v /var/run/docker.sock:/var/run/docker.sock -e HOME=/tmp'
                }
            }
            steps {
                script {
                    echo '---  Building Images (Local Cache) ---'
                    
                    sh "docker build -t ${REGISTRY}/${IMAGE_RE}:${VERSION} -t ${REGISTRY}/${IMAGE_RE}:latest backend-java/${IMAGE_RE}"
                    sh "docker build -t ${REGISTRY}/${IMAGE_PA}:${VERSION} -t ${REGISTRY}/${IMAGE_PA}:latest backend-java/${IMAGE_PA}"    
                    sh "docker build -t ${REGISTRY}/${IMAGE_FL}:${VERSION} -t ${REGISTRY}/${IMAGE_FL}:latest backend-java/${IMAGE_FL}"          

                    echo 'Images built and tagged locally. Waiting for tests...'
                }
            }
        }
        stage('Smoke Test (Compose)') {
            agent {
                docker {
                    image 'docker:latest'
                    args '-v /var/run/docker.sock:/var/run/docker.sock -v /tmp:/tmp'
                }
            }
            environment {
                DB_ACCESS = credentials('db-credentials') 
            }
            steps {
                script {
                    echo '---  Preparing Shared Test Environment ---'
                    
                    def testDir = "/tmp/jenkins-test-${BUILD_NUMBER}"
                    
                    sh """
                        mkdir -p ${testDir}
                        cp docker-compose.yml ${testDir}/
                        cp nginx.conf ${testDir}/ || true
                        cp kafka-config -r ${testDir}/
                        cp mosquitto-config -r ${testDir}/
                    """

                    echo '--- Starting Compose (Using Registry Images) ---'
                    sh """
                        cd ${testDir}
                        export POSTGRES_USER='${DB_ACCESS_USR}'
                        export POSTGRES_PASSWORD='${DB_ACCESS_PSW}'
                        export REGISTRY='${REGISTRY}'
                        export IMAGE_RE='${IMAGE_RE}'
                        export IMAGE_PA='${IMAGE_PA}'
                        export IMAGE_FL='${IMAGE_FL}'
                        docker compose up -d --no-build
                    """                    
                        
                    echo '--- Waiting for Application ---'
                    sleep 120 

                    echo '--- Testing Endpoint ---'
                    sh "wget --spider -q http://${MANAGER_IP}:80/api/map/bikes || exit 1"
                }
            }
            post {
                always {
                    script {
                        def testDir = "/tmp/jenkins-test-${BUILD_NUMBER}"
                        echo '--- Cleanup ---'
                        sh "cd ${testDir} && docker compose down || true"
                        sh "rm -rf ${testDir}"
                    }
                }
            }
        }

        stage('Push to Registry') {
            agent {
                docker {
                    image 'docker:cli'
                    args '-v /var/run/docker.sock:/var/run/docker.sock -e HOME=/tmp'
                }
            }
            steps {
                script {
                    echo '---  Pushing Images to Registry ---'
                    
                    sh "docker push --all-tags $REGISTRY/$IMAGE_RE"
                    sh "docker push --all-tags $REGISTRY/$IMAGE_PA"
                    sh "docker push --all-tags $REGISTRY/$IMAGE_FL"
                    
                    echo 'Images are in the registry!'
                }
            }
        }

        stage('Deploy to Swarm') {
            agent any
            environment {
                DB_ACCESS = credentials('db-credentials')
            }
            steps {
                sshagent([SSH_CRED_ID]) {
                    script {
                        echo '--- Deploying to Swarm Manager ---'
                        
                        sh "scp -o StrictHostKeyChecking=no docker-stack.yml vagrant@192.168.56.200:/vagrant/"
                        sh "scp -o StrictHostKeyChecking=no nginx.conf vagrant@192.168.56.200:/vagrant/"
                        sh "scp -o StrictHostKeyChecking=no -r kafka-config vagrant@192.168.56.200:/vagrant/"
                        sh "scp -o StrictHostKeyChecking=no -r mosquitto-config vagrant@192.168.56.200:/vagrant/"

                        def remoteDeployScript = """
                            set -e
                            cd /vagrant
                            
                            echo " -> Deploying stack: city-bike-system"
                            export STACK_NAME=city-bike-system
                            export POSTGRES_USER='${DB_ACCESS_USR}'
                            export POSTGRES_PASSWORD='${DB_ACCESS_PSW}'
                            export REGISTRY='${REGISTRY}'
                            export IMAGE_RE='${IMAGE_RE}'
                            export IMAGE_PA='${IMAGE_PA}'
                            export IMAGE_FL='${IMAGE_FL}'
                            
                            docker stack deploy --with-registry-auth --resolve-image=always -c docker-stack.yml city-bike-system
                        """
                        
                        sh "ssh -o StrictHostKeyChecking=no vagrant@${MANAGER_IP} '${remoteDeployScript}'"
                    }
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline Finished Successfully.'
            emailext body: """
                <p>Gratulacje! Wdrożenie zakończone sukcesem.</p>
                <p><strong>Projekt:</strong> ${env.JOB_NAME}</p>
                <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                <p><a href="${env.BUILD_URL}">Zobacz logi w Jenkinsie</a></p>
                <p>Aplikacja dostępna pod adresem: http://${MANAGER_IP}</p>
            """,
            subject: "SUKCES: Build #${env.BUILD_NUMBER}",
            to: "${USER_EMAIL}",
            mimeType: 'text/html'
        }
        failure {
            echo 'Pipeline Failed.'
            emailext body: """
                <p style="color:red;">Ups! Coś poszło nie tak.</p>
                <p><strong>Projekt:</strong> ${env.JOB_NAME}</p>
                <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                <p><a href="${env.BUILD_URL}console">Kliknij tutaj, aby zobaczyć błąd w konsoli</a></p>
            """,
            subject: "BŁĄD: Build #${env.BUILD_NUMBER}",
            to: "${USER_EMAIL}",
            mimeType: 'text/html'
        }
    }
}