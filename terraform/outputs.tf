output "beanstalk_endpoint_url" {
 value       = aws_elastic_beanstalk_environment.auto_board_env.endpoint_url
 description = "Elastic Beanstalk Environment URL."
}