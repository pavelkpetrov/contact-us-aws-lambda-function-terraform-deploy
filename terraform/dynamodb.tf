resource "aws_dynamodb_table" "contact_us_table" {
  name            = "${var.dynamodb_table_name}"
  billing_mode    = "${var.dynamodb_table_billing_mode}"
  hash_key        = "Id"
  attribute {
    name = "Id"
    type = "S"
  }
  tags = {
    environment   = "${var.inrastructure_environment}"
  }
}