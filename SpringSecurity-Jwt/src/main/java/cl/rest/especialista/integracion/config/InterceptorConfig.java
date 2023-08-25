package cl.rest.especialista.integracion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	
	@Autowired
	@Qualifier("generic")
	private HandlerInterceptor generic;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(generic).excludePathPatterns("");
	}
}
