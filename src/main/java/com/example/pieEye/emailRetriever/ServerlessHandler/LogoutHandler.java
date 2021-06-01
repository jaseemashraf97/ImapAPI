package com.example.pieEye.emailRetriever.ServerlessHandler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.pieEye.emailRetriever.Service.MailService;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class LogoutHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private final Logger logger = Logger.getLogger(this.getClass());
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            MailService mailService = new MailService();
            String status= mailService.logout();
            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(status).setHeaders(Collections.singletonMap("X-Powered-By","AWS Lambda & Serverless")).build();
        }
        catch (Exception e)
        {
            Response response = new Response("Logging out failed",input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(response)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
