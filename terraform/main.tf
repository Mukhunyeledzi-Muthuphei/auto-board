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

resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_instance" "my_instance" {
  ami           = "ami-00d6d5db7a745ff3f"  # Example Amazon Linux 2 AMI, adjust to your preferred one
  instance_type = "t3.micro"
  subnet_id     = aws_subnet.main_subnet.id
  vpc_security_group_ids = [aws_security_group.allow_ssh.id]

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

data "aws_vpc" "selected" {
  id = "vpc-0dcb467ecc7f5abd1"  # Replace with your actual VPC ID
}

resource "aws_security_group" "allow_ssh" {
  name_prefix = "allow_ssh-"
  description = "Allow SSH inbound traffic"
  vpc_id      = data.aws_vpc.selected.id

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

resource "aws_subnet" "main_subnet" {
  vpc_id                  = data.aws_vpc.selected.id  # Replace with your actual VPC ID
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "af-south-1a"
  map_public_ip_on_launch = true  # Ensures instances get a public IP

  tags = {
    Name = "my-subnet"
  }
}

output "instance_public_ip" {
  value = aws_instance.my_instance.public_ip
}
