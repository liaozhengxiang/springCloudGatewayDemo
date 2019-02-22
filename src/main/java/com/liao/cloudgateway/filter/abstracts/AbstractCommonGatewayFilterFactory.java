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
 * 需要将自定义的GatewayFilter注入SpringGateway，配置文件方式现在只能通过构建工厂的方式注入<br/>
 * 用法：<br/>
 * 1.只需要继承该类，并将实现了GatewayFilter和Orderd接口的自定义filter通过getGatewayFilter传递进来<br/>
 * 2.加上@Component标签即可，继承类需使用xxxxGatewayFilterFactory的形式<br/>
 * 3.在配置文件中使用该过滤器填写名称为：xxxx，Spring默认帮我们去掉GatewayFilterFactory部分<br/>
 *
 * @author liaozhengxiang
 * @date 2019/2/22
 */

public abstract class AbstractCommonGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AbstractCommonGatewayFilterFactory.Config> {

    public AbstractCommonGatewayFilterFactory() {
        super(Config.class);
    }

    public abstract GatewayFilter getGatewayFilter();

    /**
     * Spring gateway启动时会调用该方法，初始化过滤器并将过滤器和RouteId进行绑定
     *
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(Config config) {
        return getGatewayFilter();
    }

    /**
     * 这个类仅是为了符合规范，暂时无其他用处
     */
    static class Config {
    }
}
