package com.liao.cloudgateway.filter;

import com.liao.cloudgateway.filter.abstracts.AbstractModifyResponseBodyFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class SignRespBodyFilter extends AbstractModifyResponseBodyFilter {
    @Override
    public Mono modifyBody(ServerWebExchange exchange, Object originalBody) {
        System.out.println("modify-----"+System.currentTimeMillis());
        return Mono.just(originalBody + ":" + "我修改过了");
    }

    @Override
    public Integer getFilterOrder() {
        return 1;
    }
}
