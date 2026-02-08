#!/bin/bash

cat <<EOF | sudo tee /etc/docker/daemon.json
{
  "insecure-registries": ["192.168.56.200:5000"]
}
EOF

sudo systemctl restart docker

until [ -f /vagrant/join-token-worker.txt ]
do
    sleep 5
done

docker swarm join --token "`cat /vagrant/join-token-worker.txt`"  192.168.56.200:2377
