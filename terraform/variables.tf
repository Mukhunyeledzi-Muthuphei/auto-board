variable "db_username" {
  description = "RDS master username"
  type        = string
  default     = try(env("DB_USERNAME"), "")
}

variable "db_password" {
  description = "RDS master password"
  type        = string
  sensitive   = true
  default     = try(env("DB_PASSWORD"), "")
}

variable "region" {
  description = "AWS region"
  type        = string
  default     = "af-south-1"
}

variable "google_client_secret" {
  description = "Google Client Secret"
  type        = string
  sensitive   = true
  default     = try(env("GOOGLE_SECRET"), "")
}

