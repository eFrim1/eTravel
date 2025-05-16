package com.etravel.defaultgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthLoggingFilter implements GlobalFilter, Ordered {
    @Override
    public int getOrder() { return -1; }   // run *before* the RoutingFilter

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println(">>> GATEWAY sees Authorization: " +
                exchange.getRequest().getHeaders().getFirst("Authorization"));
        return chain.filter(exchange);
    }
}