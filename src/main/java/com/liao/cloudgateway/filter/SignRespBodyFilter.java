package com.liao.cloudgateway.filter;

import com.liao.cloudgateway.filter.abstracts.AbstractModifyResponseBodyFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * 返回商户报文加签过滤器
 * @author liaozhengxiang
 * @date 2019/2/22
 */

public class SignRespBodyFilter extends AbstractModifyResponseBodyFilter {
    @Override
    public Mono modifyBody(ServerWebExchange exchange, Object originalBody) {
        return Mono.just(originalBody + ":" + "我修改过了");
    }

    @Override
    public Integer getFilterOrder() {
        return 1;
    }
}
