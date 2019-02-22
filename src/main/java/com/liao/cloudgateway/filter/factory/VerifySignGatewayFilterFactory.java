package com.liao.cloudgateway.filter.factory;

import com.liao.cloudgateway.filter.VerifySignFilter;
import com.liao.cloudgateway.filter.abstracts.AbstractCommonGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.stereotype.Component;

/**
 * 签名校验的拦截器
 *
 * @author liaozhengxiang
 * @date 2019/2/22
 */

@Component
public class VerifySignGatewayFilterFactory extends AbstractCommonGatewayFilterFactory {
    @Override
    public GatewayFilter getGatewayFilter() {
        return new VerifySignFilter();
    }
}