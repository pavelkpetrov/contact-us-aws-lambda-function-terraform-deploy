resource "aws_s3_bucket" "website" {

  bucket = "${var.website_bucket_name}"
  acl    = "public-read"

  website {
    index_document = "index.html"
  }
}

resource "aws_s3_bucket_object" "website" {

  for_each     = fileset("${path.module}/../web-app", "**/*")
  bucket       = aws_s3_bucket.website.bucket
  key          = each.value
  content      = data.template_file.data[each.value].rendered
  etag         = filemd5("${path.module}/../web-app/${each.value}")
  content_type = "text/html"
}

data "template_file" "data" {

  for_each     = fileset("/${path.module}/../web-app", "**/*")
  template     = "${file("${path.module}/../web-app/${each.value}")}"
  vars = {
    lambda_url = "${aws_api_gateway_deployment.java_lambda_deploy.invoke_url}"
  }
}
