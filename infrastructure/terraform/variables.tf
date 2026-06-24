variable "region" {
  type    = string
  default = "us-east-1"
}

variable "credentials_profile" {
  type    = string
  default = "default"
}

variable "public_key_path" {
  description = "Path to the SSH public key (for EC2 instance access)"
  type        = string
}

variable "azs" {
  type    = list(string)
  default = ["us-east-1a", "us-east-1b"]
}

variable "unique_id" {
  description = "Suffix for Cognito domain"
  type        = string
  default     = "psi-2026"
}
