package com.est.filter;

import java.net.URI;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.est.util.GatewayUtil;

import reactor.core.publisher.Mono;

/**
 * 
 * @author Estevam Meneses
 * 
 */
@Component
public class GrpcForwardingFilter extends GatewayUtil implements GatewayFilter, Ordered {

	private static final URI TARGET_URI = URI.create("https://localhost:9099");

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		String contentType = exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);

		if (contentType != null && contentType.startsWith(GRPC_CONTENT_TYPE)) {

			// Build new URI preserving path and query
			URI originalUri = exchange.getRequest().getURI();

			URI newUri = URI.create(TARGET_URI.getScheme() + "://" + TARGET_URI.getHost() + ":" + TARGET_URI.getPort()
					+ originalUri.getRawPath()
					+ (originalUri.getRawQuery() != null ? "?" + originalUri.getRawQuery() : ""));

			// Set routing URI for NettyRoutingFilter
			exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, newUri);

			// Required to mark as routed
			exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR, TARGET_URI.getScheme());

			return chain.filter(exchange);
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 1;
	}
	
}