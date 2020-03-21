package com.liao.cloudgateway.route.repository;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从redis或者配置中心路由配置信息
 */
@Component
public class RedisRouteRepository implements RouteDefinitionRepository {
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {

        //从redis获取路由信息
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        RouteDefinition routeDefinition = new RouteDefinition();

        routeDefinition.setId("netpay");
        routeDefinition.setOrder(2);
        routeDefinition.setUri(URI.create("http://www.baidu.com"));

        //断言工厂设置
        List<PredicateDefinition> predicates = new ArrayList<>();
        PredicateDefinition netpayPredicate = new PredicateDefinition();
        netpayPredicate.setName("Path");
        Map<String, String> predicateArgs = new HashMap<>();
        predicateArgs.put("patterns", "/netpay");
        netpayPredicate.setArgs(predicateArgs);

        predicates.add(netpayPredicate);
        routeDefinition.setPredicates(predicates);

        //过滤器设置
        List<FilterDefinition> filters = new ArrayList<>();

        FilterDefinition readRequstBodyFilter = new FilterDefinition();
        readRequstBodyFilter.setName("ReadRequestBody");
        Map<String, String> filterArgs = new HashMap<>();
        readRequstBodyFilter.setArgs(filterArgs);

        filters.add(readRequstBodyFilter);
        routeDefinition.setFilters(filters);
//
        routeDefinitions.add(routeDefinition);

        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
