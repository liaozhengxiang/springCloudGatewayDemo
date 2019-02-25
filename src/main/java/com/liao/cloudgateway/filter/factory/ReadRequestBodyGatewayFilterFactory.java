package com.liao.cloudgateway.filter.factory;

import com.liao.cloudgateway.filter.ReadBodyFilter;
import com.liao.cloudgateway.filter.abstracts.AbstractCommonGatewayFilterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.stereotype.Component;

/**
 * 读取Request的Body，并将其存放到拦截器的attributes中的构建工厂
 *
 * @author liaozhengxiang
 * @date 2019/2/22
 */

@Component
public class ReadRequestBodyGatewayFilterFactory extends AbstractCommonGatewayFilterFactory {
    @Autowired
    private ReadBodyFilter readBodyFilter;

    @Override
    public GatewayFilter getGatewayFilter() {
        return readBodyFilter;
    }
}
