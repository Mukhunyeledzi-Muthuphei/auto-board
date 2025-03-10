terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.54.1"
    }
  }
}

provider "aws" {
  region = "af-south-1"  # Adjust to your preferred region
}

resource "aws_s3_bucket" "app_bucket" {
  bucket = "autoboard-artifacts"
}

resource "aws_instance" "my_instance" {
  ami           = "ami-00d6d5db7a745ff3f"  # Example Amazon Linux 2 AMI, adjust to your preferred one
  instance_type = "t3.micro"

  # Update security group to allow inbound SSH (port 22) and any other ports you may need
  security_groups = ["default"]

  tags = {
    Name = "Autoboard"
  }

  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              yum install -y aws-cli java-1.8.0-openjdk-devel
              aws s3 cp s3://autoboard-artifacts/1a7408f/autoboard-0.0.1-SNAPSHOT.jar /home/ec2-user/my-app.jar
              java -jar /home/ec2-user/my-app.jar
              EOF
}

data "aws_vpc" "default" {
  default = true
}

resource "aws_security_group" "allow_ssh" {
  name_prefix = "allow_ssh-"
  description = "Allow SSH inbound traffic"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

output "instance_public_ip" {
  value = aws_instance.my_instance.public_ip
}
