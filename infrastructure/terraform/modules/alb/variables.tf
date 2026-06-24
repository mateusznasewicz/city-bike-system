variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "ec2_instance_id" {
  description = "EC2 instance ID"
  type        = string
}

variable "subnet_ids" {
  description = "List of subnet IDs for ALB"
  type        = list(string)
}

variable "security_group_ids" {
  description = "List of security group IDs for ALB"
  type        = list(string)
}

variable "region" {
  description = "AWS region"
  type        = string
}

variable "cognito_user_pool_id" {
  description = "ID of Cognito user pool"
  type        = string
}

variable "cognito_user_pool_arn" {
  description = "ARN of Cognito user pool"
  type        = string
}

variable "cognito_user_pool_client_id" {
  description = "ID of Cognito user pool client"
  type        = string
}

variable "cognito_user_pool_public_client_id" {
  description = "ID of Cognito user pool public client"
  type        = string
}

variable "cognito_user_pool_domain" {
  description = "Domain of Cognito user pool"
  type        = string
}
