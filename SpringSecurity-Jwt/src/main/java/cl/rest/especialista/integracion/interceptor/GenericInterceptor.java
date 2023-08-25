package cl.rest.especialista.integracion.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component("generic")
public class GenericInterceptor implements HandlerInterceptor {

	protected static final Logger log = LoggerFactory.getLogger(GenericInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		log.info("Ejecuta tu logica del interceptor aqui!");
		
		return true;
	}
}
