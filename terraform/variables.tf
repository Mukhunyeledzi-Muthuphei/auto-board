variable "db_username" {
  description = "RDS master username"
  type        = string
}

variable "db_password" {
  description = "RDS master password"
  type        = string
  sensitive   = true
}

variable "region" {
  description = "AWS region"
  type        = string
  default     = "af-south-1"
}

variable "google_client_secret" {
  description = "Google Client Secret"
  type        = string
  sensitive = true
}

