variable "instance_type" { 
  type    = string 
  default = "t3.large"
}

variable "subnet_id" {
  type = string
}

variable "security_group_id" {
  type = string
}

variable "public_key_path" { 
  type    = string
}

variable "provisioning_script_path" { 
  type    = string
}
