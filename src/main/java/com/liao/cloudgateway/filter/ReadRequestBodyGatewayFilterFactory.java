/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.liao.cloudgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

import static org.springframework.cloud.gateway.filter.AdaptCachedBodyGlobalFilter.CACHED_REQUEST_BODY_KEY;

/**
 * 代码拷贝自@{@link org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory}
 *
 *
 * 拦截RequestBody里的内容，并将其保存到拦截链条的<br/>
 * Attributes中 key为ReadRequestBodyGatewayFilterFactory.CACHE_REQUEST_BODY_OBJECT_KEY
 */
@Component
public class ReadRequestBodyGatewayFilterFactory
        extends AbstractGatewayFilterFactory<ReadRequestBodyGatewayFilterFactory.Config> {
    public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";
    private final List<HttpMessageReader<?>> messageReaders;

    public ReadRequestBodyGatewayFilterFactory() {
        super(Config.class);
        this.messageReaders = HandlerStrategies.withDefaults().messageReaders();
    }


    @Override
    @SuppressWarnings("unchecked")
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            //已经读取过内容，直接跳过
            if (exchange.getAttributes().get(CACHE_REQUEST_BODY_OBJECT_KEY) != null) {
                return chain.filter(exchange);
            }
            Class inClass = String.class;
            Class outClass = String.class;


            ServerRequest serverRequest = new DefaultServerRequest(exchange, this.messageReaders);

            //TODO: flux or mono
            Mono<?> modifiedBody = serverRequest.bodyToMono(inClass).doOnNext(objectValue -> {
                System.out.println("------------" + objectValue);
                exchange.getAttributes().put(CACHE_REQUEST_BODY_OBJECT_KEY, objectValue);
            });


            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());

            // the new content type will be computed by bodyInserter
            // and then set in the request decorator
            headers.remove(HttpHeaders.CONTENT_LENGTH);

            // if the body is changing content types, set it here, to the bodyInserter will know about it
            if (config.getContentType() != null) {
                headers.set(HttpHeaders.CONTENT_TYPE, config.getContentType());
            }
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

        };
    }

    public static class Config {

        private String contentType;

        public String getContentType() {
            return contentType;
        }

        public Config setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }
    }
}
