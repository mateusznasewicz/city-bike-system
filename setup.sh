#!/bin/bash
# Configure timezone and NTP
timedatectl set-timezone Europe/Warsaw
sed -i 's/^#NTP=/NTP=pl.pool.ntp.org/' /etc/systemd/timesyncd.conf
systemctl restart systemd-timesyncd

# Change repository mirror to a local one
sed -i 's/httpredir.debian.org/ftp.pl.debian.org/g' /etc/apt/sources.list

# Preconfigure iptables-persistent to avoid prompts during installation 
echo iptables-persistent iptables-persistent/autosave_v4 boolean true | sudo debconf-set-selections
echo iptables-persistent iptables-persistent/autosave_v6 boolean true | sudo debconf-set-selections

# Update package lists and install prerequisites
apt-get update
apt-get install ca-certificates curl wget git zip unzip vim iptables iptables-persistent -y

# Install Docker key
install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
chmod a+r /etc/apt/keyrings/docker.asc

# Add Docker repository
tee /etc/apt/sources.list.d/docker.sources <<EOF
Types: deb
URIs: https://download.docker.com/linux/debian
Suites: $(. /etc/os-release && echo "$VERSION_CODENAME")
Components: stable
Signed-By: /etc/apt/keyrings/docker.asc
EOF

# Install Docker packages
apt-get update
apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin -y

# Add vagrant user to docker group to avoid using sudo
usermod -aG docker vagrant
newgrp docker

# Configure iptables for Docker Swarm (common for manager and worker)
iptables -A INPUT -p tcp --dport 7946 -j ACCEPT
iptables -A INPUT -p udp --dport 7946 -j ACCEPT
iptables -A INPUT -p udp --dport 4789 -j ACCEPT
iptables -A INPUT -p tcp --dport 9001 -j ACCEPT
netfilter-persistent save

# Start and enable Docker and containerd services
systemctl enable --now docker
systemctl enable --now containerd