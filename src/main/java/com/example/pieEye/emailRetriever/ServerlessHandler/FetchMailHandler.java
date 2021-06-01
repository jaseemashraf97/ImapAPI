package com.example.pieEye.emailRetriever.ServerlessHandler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.example.pieEye.emailRetriever.Service.MailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;

public class FetchMailHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{
    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            Map<String, Integer> pathParam = (Map<String, Integer>)input.get("id");
            int id = (int)pathParam.get("id");
            MailService mailService = new MailService();
            Object mail= mailService.fetchOneMail(id);
            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(mail).setHeaders(Collections.singletonMap("X-Powered-By","AWS Lambda & Serverless")).build();
        }
        catch (Exception e)
        {
            Response response = new Response("Not able to fetch the mail",input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(response)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}
