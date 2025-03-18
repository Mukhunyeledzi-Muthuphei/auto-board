variable "db_password" {
    description = "The password for the database."
    type        = string
    sensitive   = true
    default     = "Password123"
}