#!/bin/bash

cat <<EOF | sudo tee /etc/docker/daemon.json
{
  "insecure-registries": ["192.168.56.200:5000"]
}
EOF

# still not working
# to handle it: ssh to manager and use 'sudo chmod 666 /var/run/docker.sock' after vagrant up
echo "--- Setting Permanent Permissions for Docker Socket ---"
sudo mkdir -p /etc/systemd/system/docker.socket.d
cat <<EOF | sudo tee /etc/systemd/system/docker.socket.d/override.conf
[Socket]
SocketMode=0666
EOF

sudo systemctl daemon-reload
sudo systemctl restart docker.socket
sudo systemctl restart docker

# Setup Docker Swarm Manager Node firewall exception
iptables -A INPUT -p tcp --dport 2377 -j ACCEPT
iptables -A INPUT -p tcp --dport 9443 -j ACCEPT
iptables -A INPUT -p tcp --dport 8080 -j ACCEPT
iptables -A INPUT -p tcp --dport 50000 -j ACCEPT
netfilter-persistent save

# Initialize Docker Swarm
docker swarm init --advertise-addr 192.168.56.200

echo "--- Setting up Local Docker Registry ---"

if ! docker service ls | grep -q registry; then
    docker service create \
      --name registry \
      --publish published=5000,target=5000 \
      registry:2
    echo "Local Registry started on port 5000"
else
    echo "Registry service already exists."
fi

# Save join tokens to files for worker nodes
docker swarm join-token worker --quiet > /vagrant/join-token-worker.txt

# Deploy Portainer app for managing the Swarm
docker image pull portainer/portainer-ce:lts --quiet
docker image pull portainer/agent:lts --quiet


docker stack deploy -c /vagrant/portainer-agent-stack.yml portainer 2>/dev/null || true

echo "--- Installing Jenkins ---"

if [ ! -f /home/vagrant/.ssh/id_rsa ]; then
    echo "--- Generating SSH keys for Vagrant ---"
    ssh-keygen -t rsa -b 4096 -f /home/vagrant/.ssh/id_rsa -N "" -q
    cat /home/vagrant/.ssh/id_rsa.pub >> /home/vagrant/.ssh/authorized_keys
    ssh-keyscan -H 192.168.56.200 >> /home/vagrant/.ssh/known_hosts
    chown -R vagrant:vagrant /home/vagrant/.ssh
fi


cp /home/vagrant/.ssh/id_rsa /vagrant/jenkins/jenkins_id_rsa

docker build -t custom-jenkins /vagrant/jenkins

docker volume create jenkins_home


docker service create \
  --name jenkins \
  --replicas 1 \
  --constraint 'node.role == manager' \
  --publish published=8080,target=8080 \
  --publish published=50000,target=50000 \
  --mount type=bind,source=/var/run/docker.sock,target=/var/run/docker.sock \
  --mount type=volume,source=jenkins_home,target=/var/jenkins_home \
  custom-jenkins