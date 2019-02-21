package com.liao.cloudgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class VerifySignGatewayFilterFactory extends AbstractGatewayFilterFactory<VerifySignGatewayFilterFactory.VerifySignConfig> {
    /**
     * 从@{@link org.springframework.cloud.gateway.handler.predicate.ReadBodyPredicateFactory} 拷贝
     */
    private static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";

    public VerifySignGatewayFilterFactory() {
        super(VerifySignGatewayFilterFactory.VerifySignConfig.class);
    }

    @Override
    public GatewayFilter apply(VerifySignConfig verifySignConfig) {

        return (exchange, chain) -> {

            Object body = exchange.getAttribute(CACHE_REQUEST_BODY_OBJECT_KEY);
            //读取内容 原始内容默认有进行urlencode，需要进行一个decode
            System.out.println("verifyFilter:" + body);

            //结束请求
            if (StringUtils.isEmpty(body)) {
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };


    }

    public static class VerifySignConfig {
        // 控制是否开启签名过滤器
        private boolean enabled;

        public VerifySignConfig() {
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}