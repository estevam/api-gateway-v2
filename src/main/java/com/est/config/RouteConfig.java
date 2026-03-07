package com.est.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.est.filter.GrpcForwardingFilter;
import com.est.filter.LoggingFilter;
import com.est.filter.RestForwardingFilter;

/**
 * @author Estevam Meneses
 */
@Configuration
public class RouteConfig {
	@Bean
	RouteLocator customRouteLocator(RouteLocatorBuilder builder, LoggingFilter logFilter,
			GrpcForwardingFilter grpcFilter, RestForwardingFilter restFilter) {

		return builder.routes().route("frontend", r -> 
		r.path("/app/**").uri("https://localhost:3000"))
				.route(r ->
		r.alwaysTrue().filters(f -> {
			f.filter(logFilter);  // log all requests
		    f.filter(restFilter); // REST
			f.filter(grpcFilter); // gRPC
			return f;
			// The URI will be replaced by GATEWAY_REQUEST_URL_ATTR
		}).uri("http://localhost")).build();
	}

}
