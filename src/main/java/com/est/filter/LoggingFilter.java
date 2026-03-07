package com.est.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 
 * @author Estevam Meneses
 * 
 */
@Component
public class LoggingFilter implements GatewayFilter {

	private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("Content Type:{} Method:{} URI:{}" ,
				exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE),
				exchange.getRequest().getMethod().toString(),
				exchange.getRequest().getURI().getPath().toString());
		return chain.filter(exchange);
	}
}