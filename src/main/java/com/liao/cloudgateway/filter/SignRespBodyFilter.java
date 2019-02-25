package com.liao.cloudgateway.filter;

import com.liao.cloudgateway.enums.FilterOrder;
import com.liao.cloudgateway.filter.abstracts.AbstractModifyResponseBodyFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 返回商户报文加签过滤器
 *
 * @author liaozhengxiang
 * @date 2019/2/22
 */
@Component
public class SignRespBodyFilter extends AbstractModifyResponseBodyFilter {
    @Override
    public Mono modifyBody(ServerWebExchange exchange, Object originalBody) {
        System.out.println("数据进行签名：" + originalBody);
        return Mono.just(originalBody + ":" + "我修改过了");
    }

    @Override
    public Integer getFilterOrder() {
        return FilterOrder.SIGN_RESP.getOrder();
    }
}
