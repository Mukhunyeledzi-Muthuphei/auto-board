output "beanstalk_endpoint_url" {
 value       = aws_elastic_beanstalk_environment.auto_board_env.endpoint_url
 description = "Elastic Beanstalk Environment URL."
}

output "database_endpoint" {
 value       ="${aws_db_instance.auto-board-db.address}"
 description ="PostgreSQL DB endpoint URL."
}
