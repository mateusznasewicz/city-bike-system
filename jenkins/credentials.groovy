import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*

def keyPath = "/usr/share/jenkins/ref/jenkins_id_rsa"
def privateKeySource = new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(keyPath)

def creds = new BasicSSHUserPrivateKey(
  CredentialsScope.GLOBAL,
  "vagrant-ssh-key",       // ID używane w Jenkinsfile
  "vagrant",               // Username
  privateKeySource,        // Źródło klucza
  "",                      // Passphrase (puste)
  "Auto-generated SSH key for Deploy"
)

SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), creds)