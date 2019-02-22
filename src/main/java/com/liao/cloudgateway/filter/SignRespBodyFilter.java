package com.liao.cloudgateway.filter;

import com.liao.cloudgateway.filter.abstracts.AbstractModifyResponseBodyFilter;
import org.springframework.web.server.ServerWebExchange;

public class SignRespBodyFilter extends AbstractModifyResponseBodyFilter {
    @Override
    public Object modifyBody(ServerWebExchange exchange, Object originalBody) {
        return null;
    }

    @Override
    public Integer getFilterOrder() {
        return 1;
    }
}
