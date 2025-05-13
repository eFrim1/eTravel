package com.etravel.defaultgateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestResponseHeaderFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        // Capture the incoming X-Test-Header
        String testHeader = exchange.getRequest().getHeaders().getFirst("X-Test-Header");
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            if (testHeader != null) {
                exchange.getResponse().getHeaders().add("X-Test-Header", testHeader);
            }
        }));
    }

    @Override
    public int getOrder() {
        // Ensure it runs after routing
        return Ordered.LOWEST_PRECEDENCE;
    }
}
