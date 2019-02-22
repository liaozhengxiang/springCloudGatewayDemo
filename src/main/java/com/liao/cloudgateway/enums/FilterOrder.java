package com.liao.cloudgateway.enums;

import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;

/**
 * 过滤器的执行顺序,数字越小 优先级越高
 *
 * @author liaozhengxiang
 * @date 2019/2/22
 */

public enum FilterOrder {

    /**
     * request 部分 应小于NettyRoutingFilter.HIGHEST_PRECEDENCE
     */

    READ_REQ_BODY(NettyRoutingFilter.HIGHEST_PRECEDENCE + 100, "读取并缓存请求body过滤器"),
    VERIFY_SIGN(NettyRoutingFilter.HIGHEST_PRECEDENCE + 101, "验正商户签名过滤器"),


    /**
     * response 部分，应大于NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER
     */
    SIGN_RESP(NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 101, "对返回商户的信息进行加签");


    private Integer order;
    private String desc;

    FilterOrder(Integer order, String desc) {
        this.order = order;
        this.desc = desc;
    }

    public Integer getOrder() {
        return order;
    }

    public String getDesc() {
        return desc;
    }}
