variable "unique_id" {
  description = "Unique suffix for the Cognito domain"
  type        = string
}

variable "alb_dns_name" {
  description = "DNS of the ALB for the callback URL"
  type        = string
}
