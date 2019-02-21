package com.liao.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class CloudgatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudgatewayApplication.class, args);
    }






  /*  @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        System.out.println("customRouteLocator........");
        return builder.routes()
                .route("pathRoute", r -> r.path("/fastPay/**")
                        .and()
                        //表示需要读取body，spring会调用ReadBodyPredicateFactory缓存body
                        .readBody(String.class, readeBody -> true)
//                        .filters(f->f.filter().filter())
                        .uri("http://httpbin.org"))

//                .route("pathRoute", r -> r.path("/fastPay/**")
//                        .filters(f->f.modifyRequestBody())

                .build();

    }*/

}

