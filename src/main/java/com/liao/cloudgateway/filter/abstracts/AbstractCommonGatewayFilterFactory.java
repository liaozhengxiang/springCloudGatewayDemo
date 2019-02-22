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

package com.liao.cloudgateway.filter.abstracts;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

/**
 * 需要将自定义的GatewayFilter注入SpringGateway，
 * 只需要继承该类，并将实现了GatewayFilter和Orderd接口的自定义filter通过getGatewayFilter传递进来
 * 并加上@Component标签即可，继承类建议使用xxxxGatewayFilterFactory的形式
 * 配置文件方式现在只能通过构建工厂的方式注入
 * @author liaozhengxiang
 * @date 2019/2/22
 */

public abstract class AbstractCommonGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AbstractCommonGatewayFilterFactory.Config> {

    public AbstractCommonGatewayFilterFactory(){
        super(Config.class);
    }
    public abstract GatewayFilter getGatewayFilter();


    @Override
    public GatewayFilter apply(Config config) {
        return getGatewayFilter();
    }


    public static class Config {
    }
}
