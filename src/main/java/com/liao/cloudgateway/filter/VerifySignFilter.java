package com.liao.cloudgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class VerifySignFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Object body = exchange.getAttribute(ReadBodyFilter.CACHE_REQUEST_BODY_OBJECT_KEY);

        //对数据进行验签
        System.out.println("数据验签：--" + body);

        return chain.filter(exchange);
    }

    /**
     * 这里的Order需要比ReadBody大，这样才能读取到数据
     *
     * @return
     */
    @Override
    public int getOrder() {
        return NettyRoutingFilter.HIGHEST_PRECEDENCE + 101;
    }
}
