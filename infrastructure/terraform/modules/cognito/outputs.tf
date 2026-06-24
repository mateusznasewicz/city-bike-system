output "user_pool_id" {
  value = aws_cognito_user_pool.main.id
}

output "user_pool_arn" {
  value = aws_cognito_user_pool.main.arn
}

output "user_pool_client_id" {
  value = aws_cognito_user_pool_client.main.id
}

output "user_pool_domain" {
  value = aws_cognito_user_pool_domain.main.domain
}

output "user_pool_client_secret" {
  value     = aws_cognito_user_pool_client.main.client_secret
  sensitive = true
}

output "user_pool_public_client_id" {
  value = aws_cognito_user_pool_client.public.id
}
