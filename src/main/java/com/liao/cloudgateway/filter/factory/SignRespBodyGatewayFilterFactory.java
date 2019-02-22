package com.liao.cloudgateway.filter.factory;

import com.liao.cloudgateway.filter.SignRespBodyFilter;
import com.liao.cloudgateway.filter.abstracts.AbstractCommonGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.stereotype.Component;

/**
 * 将返回商户的报文进行加签
 *
 * @author liaozhengxiang
 * @date 2019/2/22
 */

@Component
public class SignRespBodyGatewayFilterFactory extends AbstractCommonGatewayFilterFactory {

    @Override
    public GatewayFilter getGatewayFilter() {
        return new SignRespBodyFilter();
    }
}
