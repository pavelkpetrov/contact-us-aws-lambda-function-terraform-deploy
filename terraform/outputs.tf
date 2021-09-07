output "contact_us_form_website_endpoint" {
  value       = aws_s3_bucket.website.website_endpoint
  description = "Contact us form URL."
}

output "contact_us_lambda_url" {
  value       = "${aws_api_gateway_deployment.java_lambda_deploy.invoke_url}"
  description = "Contact us lambda function URL."
}