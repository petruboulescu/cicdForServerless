package com.serverless.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.ServiceService;
import com.serverless.dao.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public class DeleteServiceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String serviceId = pathParameters.get("id");
            Boolean success = new ServiceService().delete(serviceId);
            if (success) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(204)
                        .build();
            } else {
                return ApiGatewayResponse.builder()
                        .setStatusCode(404)
                        .setObjectBody("Service with id: '" + serviceId + "' not found.")
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Error in deleting service: " + ex.getMessage());
            Response responseBody = new Response("Error in deleting service: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .build();
        }
    }
}
