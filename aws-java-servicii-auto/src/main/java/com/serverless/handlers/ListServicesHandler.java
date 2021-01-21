package com.serverless.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.ServiceService;
import com.serverless.dao.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ListServicesHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            List<Service> serviceList = new ServiceService().list();
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(serviceList)
                    .build();
        } catch (Exception ex) {
            logger.error("Error in listing services: " + ex.getMessage());
            return ApiGatewayResponse.builder()
                    .setStatusCode(400)
                    .setObjectBody("No services found")
                    .build();
        }
    }
}
