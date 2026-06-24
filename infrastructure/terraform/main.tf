# 1. Network Layer
module "network" {
  source = "./modules/network"
  azs    = var.azs
}

# 2. Security Layer
module "security" {
  source = "./modules/security"
  vpc_id = module.network.vpc_id
}

# 3. Backend (EC2)
module "ec2" {
  source                   = "./modules/ec2"
  subnet_id                = module.network.public_subnet_ids[0]
  security_group_id        = module.security.backend_sg_id
  public_key_path          = var.public_key_path
  provisioning_script_path = "ec2-provisioning.sh"
}

# 4. Identity (Cognito)
module "cognito" {
  source       = "./modules/cognito"
  unique_id    = var.unique_id
  alb_dns_name = module.alb.alb_dns_name
}

# 5. ALB
module "alb" {
  source                             = "./modules/alb"
  vpc_id                             = module.network.vpc_id
  ec2_instance_id                    = module.ec2.instance_id
  subnet_ids                         = module.network.public_subnet_ids
  security_group_ids                 = [module.security.alb_sg_id]
  region                             = var.region
  cognito_user_pool_id               = module.cognito.user_pool_id
  cognito_user_pool_arn              = module.cognito.user_pool_arn
  cognito_user_pool_client_id        = module.cognito.user_pool_client_id
  cognito_user_pool_public_client_id = module.cognito.user_pool_public_client_id
  cognito_user_pool_domain           = module.cognito.user_pool_domain
}
