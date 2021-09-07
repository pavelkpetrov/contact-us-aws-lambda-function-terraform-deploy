variable "aws_access_key" {
  # set aws access key
}

variable "aws_secret_key" {
  # set aws secret key
}

variable "region" {
  # set aws region
  default = "eu-west-1"
}

variable "lambda_payload_filename" {
  default = "../build/libs/contact-us-aws-lambda-function-terraform-deploy-1.0-SNAPSHOT.jar"
}

variable "lambda_function_handler" {
  default = "com.aws.lambda.App"
}

variable "lambda_runtime" {
  default = "java8"
}

variable "api_path" {
  default = "{proxy+}"
}

variable "api_env_stage_name" {
  default = "terraform-lambda-java-stage"
}

variable "dynamodb_table_name" {
  description = "Dynamodb table name (space is not allowed)"
  default     = "ContactUsTable"
}

variable "dynamodb_table_billing_mode" {
  description = "Controls how you are charged for read and write throughput and how you manage capacity."
  default     = "PAY_PER_REQUEST"
}

variable "inrastructure_environment" {
  description = "Name of environment"
  default     = "test"
}

variable "website_bucket_name" {
  description = "s3 bucket name"
  default     = "contact-us-app"
}
