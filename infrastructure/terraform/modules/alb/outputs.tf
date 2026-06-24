output "alb_dns_name" {
  description = "ALB DNS name"
  value       = aws_lb.main.dns_name
}
