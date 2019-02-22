package com.liao.cloudgateway.filter.factory;

import com.liao.cloudgateway.filter.abstracts.AbstractCommonGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.stereotype.Component;

@Component
public class VerifySignGatewayFilterFactory extends AbstractCommonGatewayFilterFactory {
    @Override
    public GatewayFilter getGatewayFilter() {
        return null;
    }
}