package com.mandiri.cctv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.List;
import java.util.Map;

@Configuration
public class WebMvcConfig {

    // SpaController's "/{path:[^.]*}/**" intercepts /asset/video.mp4 because
    // "asset" has no dot, causing the SPA forward instead of the file being served.
    // This handler runs at order -1 (before RequestMappingHandlerMapping at 0),
    // so /asset/** is resolved from the classpath before SpaController sees it.
    @Bean
    public ResourceHttpRequestHandler assetResourceHandler() {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(List.of(new ClassPathResource("static/asset/")));
        return handler;
    }

    @Bean
    public SimpleUrlHandlerMapping assetHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(-1);
        mapping.setUrlMap(Map.of("/asset/**", assetResourceHandler()));
        return mapping;
    }
}
