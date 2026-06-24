# -------------------
# Terraform Configuration
# -------------------
terraform {
  
  # Ensure Terraform version is at least 1.5.0
  required_version = ">= 1.5.0"
  
  # Specify required providers and their versions
  required_providers {
    aws = {
      # Provider source: official Hashicorp AWS provider
      source  = "hashicorp/aws"

      # Use AWS provider version 6.x (any compatible minor version)
      version = "~> 6.0"
    }
  }
}


# -------------------
# AWS Provider Configuration
# -------------------
provider "aws" {
  # AWS region to deploy resources into
  region  = var.region

  # AWS CLI profile to use for credentials (from ~/.aws/credentials)
  profile = var.credentials_profile
}
