output "endpoint_url" {
  description = "The URL of the application"
  value       = "https://${module.alb.alb_dns_name}"
}

output "ssh_command" {
  description = "Command to SSH into the backend"
  value       = "ssh -i ${replace(var.public_key_path, ".pub", "")} ubuntu@${module.ec2.public_ip}"
}

output "cognito_client_id" {
  value = module.cognito.user_pool_client_id
}

output "cognito_public_client_id" {
  value = module.cognito.user_pool_public_client_id
}

output "kafka_broker_public" {
  value = "${module.ec2.public_ip}:9092"
}
