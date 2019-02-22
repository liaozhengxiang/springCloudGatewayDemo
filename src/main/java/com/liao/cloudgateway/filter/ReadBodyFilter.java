package com.liao.cloudgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class ReadBodyFilter implements GatewayFilter, Ordered {
    public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";
    private final List<HttpMessageReader<?>> messageReaders=HandlerStrategies.withDefaults().messageReaders();

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        //已经读取过内容，直接跳过
        if (exchange.getAttributes().get(CACHE_REQUEST_BODY_OBJECT_KEY) != null) {
            return chain.filter(exchange);
        }
        Class inClass = String.class;
        Class outClass = String.class;


        ServerRequest serverRequest = new DefaultServerRequest(exchange, this.messageReaders);

        //TODO: flux or mono
        Mono<?> modifiedBody = serverRequest.bodyToMono(inClass).doOnNext(objectValue -> {
            readBody(exchange, objectValue);
        });


        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());

        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);


        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                // .log("modify_request", Level.INFO)
                .then(Mono.defer(() -> {
                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                            exchange.getRequest()) {
                        @Override
                        public HttpHeaders getHeaders() {
                            long contentLength = headers.getContentLength();
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.putAll(super.getHeaders());
                            if (contentLength > 0) {
                                httpHeaders.setContentLength(contentLength);
                            } else {
                                // TODO: this causes a 'HTTP/1.1 411 Length Required' on httpbin.org
                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                            }
                            return httpHeaders;
                        }

                        @Override
                        public Flux<DataBuffer> getBody() {
                            return outputMessage.getBody();
                        }
                    };
                    return chain.filter(exchange.mutate().request(decorator).build());
                }));


    }

    /**
     * 值越低 优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return NettyRoutingFilter.HIGHEST_PRECEDENCE + 1;
    }

    public void readBody(ServerWebExchange exchange, Object body) {
        exchange.getAttributes().put(CACHE_REQUEST_BODY_OBJECT_KEY, body);
    }
}
