terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  region = "af-south-1"
}

resource "aws_elastic_beanstalk_application" "auto_board" {
  name        = "auto-board"
  description = "Auto board application for automating task management"
}

resource "aws_iam_role" "beanstalk_role" {
  name = "beanstalk-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      },
    ],
  })
}

// Database instance creation
resource "aws_db_instance" "auto-board-db" {
  db_instance_identifier = "auto-board-db-instance"
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "postgres"
  engine_version       = "17.1"
  instance_class       = "db.t3.micro"
  username             = "postgres"
  password             = "Password123"        # Replace with a strong password
  parameter_group_name = "default.postgres17" # Adjust if needed
  skip_final_snapshot  = true                 # For testing; remove in production
  publicly_accessible  = false                #best practice

  vpc_security_group_ids = [aws_security_group.rds_sg.id]

  # Subnet group is required for multi az and recommended for single az.
  db_subnet_group_name = aws_db_subnet_group.rds_subnet_group.name

}

resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "rds-subnet-group"
  subnet_ids = [aws_subnet.subnet_a.id, aws_subnet.subnet_b.id]
}

resource "aws_security_group" "rds_sg" {
  name        = "rds-sg"
  description = "Security group for RDS instance"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.beanstalk_sg.id] #allow traffic from beanstalk.
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "beanstalk_sg" {
  name        = "beanstalk-sg"
  description = "Security group for beanstalk"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = 0
    to_port     = 65535
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # allow traffic from anywhere - adjust for security
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

}

resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "subnet_a" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "af-south-1a"
}

resource "aws_subnet" "subnet_b" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "af-south-1b"
}

// User policies

resource "aws_iam_role_policy_attachment" "beanstalk_web_tier" {
  role       = aws_iam_role.beanstalk_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier"
}

resource "aws_iam_instance_profile" "beanstalk_instance_profile" {
  name = "beanstalk-instance-profile"
  role = aws_iam_role.beanstalk_role.name
}

resource "aws_s3_bucket" "beanstalk_bucket" {
  bucket = "beanstalk-bucket-example"
}

# resource "aws_s3_object" "beanstalk_zip" {
#   bucket = aws_s3_bucket.beanstalk_bucket.id
#   key    = "my-application.zip"
#   source = "../my-application.zip" #local path to zip file
# }
#
# resource "aws_elastic_beanstalk_application_version" "example" {
#   name        = "v1"
#   application = aws_elastic_beanstalk_application.auto_board.name
#   bucket      = aws_s3_bucket.beanstalk_bucket.id
#   key         = aws_s3_object.beanstalk_zip.key
# }
#
# resource "aws_elastic_beanstalk_environment" "auto_board_env" {
#   name                = "auto-board-env"
#   application         = aws_elastic_beanstalk_application.auto_board.name
#   solution_stack_name = "64bit Amazon Linux 2023 v4.4.4 running Corretto 21"
#   version_label       = aws_elastic_beanstalk_application_version.example.name
#
#   setting {
#     namespace = "aws:autoscaling:launchconfiguration"
#     name      = "InstanceType"
#     value     = "t3.micro"
#   }
#
#   setting {
#     namespace = "aws:autoscaling:launchconfiguration"
#     name      = "IamInstanceProfile"
#     value     = aws_iam_instance_profile.beanstalk_instance_profile.arn
#   }
#
#   setting {
#     namespace = "aws:ec2:vpc"
#     name      = "VPCId"
#     value     = "vpc-019dcf1180ff57389"
#   }
#
#   setting {
#     namespace = "aws:ec2:vpc"
#     name      = "Subnets"
#     value     = "subnet-096ddbd84d170a394,subnet-0add9f41b736e54a6,subnet-05356ef13c7540e5e"
#   }
#
#   setting {
#     namespace = "aws:ec2:vpc"
#     name      = "ELBSubnets"
#     value     = "subnet-096ddbd84d170a394,subnet-0add9f41b736e54a6,subnet-05356ef13c7540e5e"
#   }
#
#   setting {
#     namespace = "aws:ec2:vpc"
#     name      = "ELBScheme"
#     value     = "internet-facing" # or internal
#   }
#
#   setting {
#     namespace = "aws:autoscaling:launchconfiguration"
#     name      = "SecurityGroups"
#     value     = "sg-04d4ae7fe299a3df8"
#   }
#
#   setting {
#     namespace = "aws:elasticbeanstalk:environment"
#     name      = "ServiceRole"
#     value     = "AWSServiceRoleForElasticBeanstalk"
#   }
#
#   setting {
#     namespace = "aws:elbv2:loadbalancer"
#     name      = "SecurityGroups"
#     value     = "sg-04d4ae7fe299a3df8"
#   }
#
#   setting {
#     namespace = "aws:elasticbeanstalk:application:environment"
#     name      = "PORT"
#     value     = "8080" # Match your Java app's listening port
#   }
#
#   setting {
#     namespace = "aws:elbv2:listener:default"
#     name      = "ListenerEnabled"
#     value     = "true"
#   }
# }