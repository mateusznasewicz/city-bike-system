# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  config.vm.box = "bento/debian-12.9"

  config.vm.provider "virtualbox" do |vb|
    vb.cpus = 2
    vb.memory = 2048
  end

  config.vm.provision "shell", path: "setup.sh"

  config.vm.define "manager", primary: true do |manager|
    manager.vm.network "private_network", ip: "192.168.56.200"
    manager.vm.hostname = "manager"
    manager.vm.provision "shell", path: "setup-manager.sh"
  end

  config.vm.define "worker1" do |worker1|
    worker1.vm.network "private_network", ip: "192.168.56.201"
    worker1.vm.hostname = "worker1"
    worker1.vm.provision "shell", path: "setup-worker.sh"  

    worker1.vm.provider "virtualbox" do |vb|
      vb.memory = 4096
      vb.cpus = 4
    end
  end

end
