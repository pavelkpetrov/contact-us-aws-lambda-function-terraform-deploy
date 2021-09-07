# lambda role
resource "aws_iam_role" "iam_role_for_lambda" {
  name               = "lambda-invoke-role"
  assume_role_policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Action": "sts:AssumeRole",
        "Principal": {
          "Service": "lambda.amazonaws.com"
        },
        "Effect": "Allow",
        "Sid": ""
      }
    ]
}
EOF
}

# lambda policy
resource "aws_iam_policy" "iam_policy_for_lambda" {
  name   = "lambda-invoke-policy"
  path   = "/"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Sid": "LambdaPolicy",
        "Effect": "Allow",
        "Action": [
          "cloudwatch:PutMetricData",
          "ec2:DescribeNetworkInterfaces",
          "ec2:CreateNetworkInterface",
          "ec2:DeleteNetworkInterface",
          "logs:CreateLogStream",
          "logs:PutLogEvents",
          "xray:PutTelemetryRecords",
          "xray:PutTraceSegments"
        ],
        "Resource": "*"
      },
      {
        "Effect": "Allow",
        "Action": "dynamodb:List*",
        "Resource": "*"
      },
      {
        "Effect": "Allow",
        "Action": [
                   "dynamodb:Get*",
                   "dynamodb:PutItem",
                   "dynamodb:DescribeTable"
                  ],
        "Resource": "${aws_dynamodb_table.contact_us_table.arn}"
      }

    ]
  }
EOF
}

# Attach the policy to the role
resource "aws_iam_role_policy_attachment" "aws_iam_role_policy_attachment" {
  role       = "${aws_iam_role.iam_role_for_lambda.name}"
  policy_arn = "${aws_iam_policy.iam_policy_for_lambda.arn}"
}

resource "aws_s3_bucket_policy" "website" {
  bucket = aws_s3_bucket.website.id

  # Terraform's "jsonencode" function converts a
  # Terraform expression's result to valid JSON syntax.
  policy = jsonencode({
    Version   = "2012-10-17"
    Id        = "iam_policy_for_s3_website"
    Statement = [
      {
        Sid       = "PublicReadGetObject"
        Effect    = "Allow"
        Principal = "*"
        Action    = "s3:GetObject"
        Resource  = "arn:aws:s3:::${var.website_bucket_name}/*"
      },
    ]
  })
}
