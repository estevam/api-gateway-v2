package com.est.filter;

import java.net.URI;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter implements GatewayFilter {

	private final RateLimiter rateLimiter;
	private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);
	
	public RateLimitFilter() {
		this.rateLimiter = RateLimiter.of("gateway", RateLimiterConfig.custom().limitForPeriod(2)
				.limitRefreshPeriod(Duration.ofSeconds(10)).timeoutDuration(Duration.ZERO).build());
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		// Build new URI preserving path and query
		URI originalUri = exchange.getRequest().getURI();
		
		log.info(">>>Rete limit URI:{}" ,originalUri); 
		log.info(">>>Rete limit Path:{}" ,originalUri.getRawPath()); 
		
		return Mono.fromCallable(() -> rateLimiter.acquirePermission()).flatMap(allowed -> {
			if (!allowed) {
				exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
				return exchange.getResponse().setComplete();
			}
			return chain.filter(exchange);
		});
	}
}