package com.aws.lambda;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aws.lambda.exception.ContactUsLambdaClientException;
import com.aws.lambda.model.ContactUsProxyResponse;
import com.aws.lambda.model.ContactUsRequest;
import com.aws.lambda.service.AwsClientFactory;
import com.aws.lambda.service.DbService;
import com.aws.lambda.service.RequestService;
import com.aws.lambda.service.ResponseService;

import java.util.Optional;
import java.util.UUID;

import static java.lang.Boolean.FALSE;

/**
 * Request ad response types can be Input and Output stream or any custom objects.
 * Using streams directly helps to avoid different handling
 * when executing Lambda locally and in AWS environment.
 */
public class App implements RequestHandler<AwsProxyRequest, ContactUsProxyResponse> {

    // Use static variables to keep a context between executions
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AwsClientFactory awsClientFactory = new AwsClientFactory();
    private final DbService dbService = new DbService(awsClientFactory.getDynamoDB());
    private final ResponseService responseService = new ResponseService(objectMapper);
    private final RequestService requestService = new RequestService(objectMapper);

    // Use custom logger or logger from a Lambda context by
    // calling context.getLogger()

    /**
     * Send ContactUs form data to a specific email
     * and put the form data into database.
     * <p>
     *
     * @param request API Gateway request
     * @param context Lambda context
     * @return Proxy response to API Gateway
     */
    @Override
    public ContactUsProxyResponse handleRequest(AwsProxyRequest request, Context context) {
        LambdaLogger LOG = context.getLogger();
        try {
            LOG.log("Request received.");
            LOG.log(requestService.getAsPrettyString(request));

            return isWarmUpRequest(request)
                    ? handleWarmUpRequest(LOG)
                    : handleRealRequest(request, context);
        } catch (ContactUsLambdaClientException e) {
            LOG.log("Request was not handled due to a client error." + e);
            return responseService.buildResponse(400, "Client error.");
        } catch (Exception e) {
            LOG.log("Request was not handled due to a server error:" + e);
            return responseService.buildResponse(500, "Server error.");
        }
    }

    private ContactUsProxyResponse handleRealRequest(AwsProxyRequest request, Context context) {
        // Parsing an input request
        ContactUsRequest contactUsRequest = requestService.getContactUsRequest(request);
        LambdaLogger LOG = context.getLogger();

        // Sending email to an agent
        String messageId = UUID.randomUUID().toString();
        LOG.log("ContactUs message has id: " + messageId);

        // Saving request to DB
        dbService.putContactUsRequest(messageId, contactUsRequest);
        LOG.log("ContactUsRequest has been written to DB.");

        return responseService.buildResponse(200,
                String.format("Message %s has been sent successfully.", messageId));
    }

    private Boolean isWarmUpRequest(AwsProxyRequest request) {
        return Optional.ofNullable(request.getMultiValueHeaders())
                       .map(headers -> headers.containsKey("X-WARM-UP"))
                       .orElse(FALSE);
    }

    /**
     * Just to load a classpath and initialize all static fields for next calls.
     * Skips real AWS connections.
     *
     * @return Stub response
     */
    private ContactUsProxyResponse handleWarmUpRequest(LambdaLogger LOG) {
        LOG.log("Lambda was warmed up.");
        return responseService.buildWarmUpResponse();
    }

}