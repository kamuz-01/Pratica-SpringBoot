package org.Pratica_SpringBoot.Config.ConfigWebApp;

import org.Pratica_SpringBoot.Loggings.InterceptorLoggingApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

    private final InterceptorLoggingApi interceptorLoggingApi;

    public WebConfig(InterceptorLoggingApi interceptorLoggingApi) {
		this.interceptorLoggingApi = interceptorLoggingApi;
	}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorLoggingApi);
    }
}