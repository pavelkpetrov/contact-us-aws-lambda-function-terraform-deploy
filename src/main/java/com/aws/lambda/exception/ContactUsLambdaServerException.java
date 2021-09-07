package com.aws.lambda.exception;

public class ContactUsLambdaServerException extends RuntimeException {

    public ContactUsLambdaServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
