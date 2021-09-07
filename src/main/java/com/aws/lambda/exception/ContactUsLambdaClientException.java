package com.aws.lambda.exception;

public class ContactUsLambdaClientException extends RuntimeException {

    public ContactUsLambdaClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
