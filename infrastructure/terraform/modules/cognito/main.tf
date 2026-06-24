resource "aws_cognito_user_pool" "main" {
  name = "city-bike-system-cognito-user-pool"

  username_attributes = ["email"] 
  auto_verified_attributes = ["email"]

  password_policy {
    minimum_length    = 8
    require_lowercase = false
    require_numbers   = false
    require_symbols   = false
    require_uppercase = false
  }
}

resource "aws_cognito_user_pool_client" "main" {
  name         = "city-bike-system-app-client"
  user_pool_id = aws_cognito_user_pool.main.id

  generate_secret     = true
  explicit_auth_flows = [
    "ALLOW_USER_PASSWORD_AUTH",
    "ALLOW_REFRESH_TOKEN_AUTH",
    "ALLOW_USER_SRP_AUTH"
  ]

  supported_identity_providers = ["COGNITO"]

  allowed_oauth_flows_user_pool_client = true
  allowed_oauth_flows                  = ["code"]
  allowed_oauth_scopes                 = ["openid", "email", "profile"]
  
  callback_urls = ["https://${var.alb_dns_name}/oauth2/idpresponse"]
}

resource "aws_cognito_user_pool_domain" "main" {
  domain       = "city-bike-system-${var.unique_id}"
  user_pool_id = aws_cognito_user_pool.main.id
}

resource "aws_cognito_user_pool_client" "public" {
  name         = "city-bike-system-cognito-public-client"
  user_pool_id = aws_cognito_user_pool.main.id

  generate_secret     = false
  explicit_auth_flows = [
    "ALLOW_USER_PASSWORD_AUTH",
    "ALLOW_REFRESH_TOKEN_AUTH",
    "ALLOW_USER_SRP_AUTH"
  ]

  supported_identity_providers = ["COGNITO"]
  allowed_oauth_flows          = ["code", "implicit"]
  allowed_oauth_scopes         = ["openid", "email", "profile"]
  callback_urls                = ["https://${var.alb_dns_name}/oauth2/idpresponse"]
}
