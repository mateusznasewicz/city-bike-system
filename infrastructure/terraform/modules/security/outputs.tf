output "alb_sg_id" {
  description = "ALB security group ID"
  value       = aws_security_group.alb.id
}

output "backend_sg_id" {
  description = "Backend security group ID"
  value       = aws_security_group.backend.id
}
