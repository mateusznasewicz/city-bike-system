# Generate a self-signed SSL certificate
resource "tls_private_key" "main" {
  algorithm = "RSA"
}

resource "tls_self_signed_cert" "main" {
  private_key_pem = tls_private_key.main.private_key_pem

  subject {
    common_name  = aws_lb.main.dns_name
    organization = "City Bike System"
  }

  validity_period_hours = 8760 # 1 year
  allowed_uses          = ["key_encipherment", "digital_signature", "server_auth"]
}

resource "aws_acm_certificate" "alb_cert" {
  private_key      = tls_private_key.main.private_key_pem
  certificate_body = tls_self_signed_cert.main.cert_pem
}

# --- ALB ---
resource "aws_lb" "main" {
  name                       = "city-bike-system-alb"
  internal                   = false
  load_balancer_type         = "application"
  security_groups            = var.security_group_ids
  subnets                    = var.subnet_ids
  enable_deletion_protection = false  # Disable deletion protection
}

resource "aws_lb_target_group" "backend" {
  name        = "city-bike-system-alb-tg-backend"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "instance"
}

resource "aws_lb_target_group_attachment" "backend" {
  target_group_arn = aws_lb_target_group.backend.arn
  target_id        = var.ec2_instance_id
  port             = 80
}

# HTTP listener → redirect to HTTPS
resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "redirect"

    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}

# --- 1. DEFAULT ACTION (For Swagger / Browser) ---
resource "aws_lb_listener" "https" {
  load_balancer_arn = aws_lb.main.arn
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = aws_acm_certificate.alb_cert.arn

  default_action {
    type  = "authenticate-cognito"
    order = 1

    authenticate_cognito {
      user_pool_arn       = var.cognito_user_pool_arn
      user_pool_client_id = var.cognito_user_pool_client_id
      user_pool_domain    = var.cognito_user_pool_domain
      # Implicitly uses on_unauthenticated_request = "authenticate"
    }
  }

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.backend.arn
    order            = 2
  }
}

# --- 2. MOBILE RULE (For JWT / Mobile App) ---
resource "aws_lb_listener_rule" "mobile_jwt_auth" {
  listener_arn = aws_lb_listener.https.arn
  priority     = 1  # Highest priority

  condition {
    path_pattern {
      values = ["/api/*"]
    }
  }

  action {
    type  = "jwt-validation"
    order = 1

    jwt_validation {
      # The Issuer for Cognito
      issuer        = "https://cognito-idp.${var.region}.amazonaws.com/${var.cognito_user_pool_id}"
      
      # The correct argument name from the docs
      jwks_endpoint = "https://cognito-idp.${var.region}.amazonaws.com/${var.cognito_user_pool_id}/.well-known/jwks.json"

      additional_claim {
        format = "single-string"
        name   = "client_id"
        values = [var.cognito_user_pool_public_client_id]
      }
    }
  }

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.backend.arn
    order            = 2
  }
}
