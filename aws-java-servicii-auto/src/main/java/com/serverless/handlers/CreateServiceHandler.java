package com.serverless.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.ServiceService;
import com.serverless.dao.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;


public class CreateServiceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {


    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            Service service = new Service();
            ServiceService serviceService= new ServiceService();
            service.setName(body.get("name").asText());
            service.setPrice(body.get("price").asDouble());
            serviceService.save(service);
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(service)
                    .build();
        } catch (Exception ex) {
            logger.error("Error in saving service: " + ex.getMessage());
            Response responseBody = new Response("Error in saving service: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .build();
        }
    }
}
