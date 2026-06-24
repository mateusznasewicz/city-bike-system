# Find the latest Ubuntu 22.04 AMI
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]  # Canonical's official AWS account ID

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

resource "aws_key_pair" "deployer" {
  key_name   = "city-bike-system-aws-ssh-key"
  public_key = file(var.public_key_path)
}

resource "aws_instance" "main" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = var.instance_type
  subnet_id              = var.subnet_id
  vpc_security_group_ids = [var.security_group_id]
  key_name               = aws_key_pair.deployer.key_name

  root_block_device {
    volume_size = 25
    volume_type = "gp3"
  }

  user_data = file(var.provisioning_script_path)

  tags = {
    Name = "city-bike-system-backend"
  }
}
