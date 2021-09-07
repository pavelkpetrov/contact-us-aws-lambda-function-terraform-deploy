package com.aws.lambda.model;

public class ContactUsResponseBody {
    private final String response;

    public ContactUsResponseBody(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
