package com.liao.cloudgateway.filter;

import com.liao.cloudgateway.enums.FilterOrder;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
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
        return FilterOrder.VERIFY_SIGN.getOrder();
    }
}
