package com.est.filter;

import static org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER;

import java.net.URI;
import java.util.Optional;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.est.cache.EndpointCache;
import com.est.entity.Endpoint;
import com.est.util.GatewayUtil;

import reactor.core.publisher.Mono;

@Component
public class RestForwardingFilter extends GatewayUtil implements GatewayFilter, Ordered {

	/**
	 * Process the Web request and (optionally) delegate to the next
	 * {@code WebFilter} through the given {@link GatewayFilterChain}.
	 * 
	 * @param exchange the current server exchange
	 * @param chain    provides a way to delegate to the next filter
	 * @return {@code Mono<Void>} to indicate when request processing is complete
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// log.info("ServerWebExchange: {}", exchange.toString());
		// log.info("GatewayFilterChain: {}", chain.toString());application/json
		String contentType = exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);

		if (contentType != null && contentType.startsWith(REST_CONTENT_TYPE)) {

			String path = exchange.getRequest().getURI().getPath().toString();
			String method = exchange.getRequest().getMethod().toString();
			Optional<Endpoint> endpoint = EndpointCache.getInstance().findEndpoint(method, path);
			// String forwardUrl =
			// exchange.getRequest().getHeaders().get(RouteConfig.FORWARDED_URL).get(0);
			exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR,
					URI.create(endpoint.get().getUrl_destination()));
			// return chain.filter(exchange);

			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				// ServerHttpResponse response = exchange.getResponse();
				// Manipulate the response in some way
				// log.info("response {}", response.getHeaders().toString());

			}));

		}
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return ROUTE_TO_URL_FILTER_ORDER + 1;
	}
}