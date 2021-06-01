package com.example.pieEye.emailRetriever.ServerlessHandler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.example.pieEye.emailRetriever.Model.MailStructure;
import com.example.pieEye.emailRetriever.Service.MailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
public class LoginHandler implements RequestHandler<Map<String,Object>,ApiGatewayResponse> {
    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String,Object> input, Context context)
    {
        try {
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            String email = body.get("email").asText();
            String password = body.get("password").asText();
            MailService mailService = new MailService();
            List<MailStructure> mails= mailService.fetchallMails(email,password);
            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(mails).setHeaders(Collections.singletonMap("X-Powered-By","AWS Lambda & Serverless")).build();
        }
        catch (Exception ex)
        {
            Response responseBody = new Response("Error in fetching the mail ", input);
            return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody).setHeaders(Collections.singletonMap("Powered by","AWS Lambda & Serverless")).build();
        }
    }
}
