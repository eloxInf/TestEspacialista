package cl.rest.especialista.integracion.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import cl.rest.especialista.integracion.errors.TokenException;
import cl.rest.especialista.integracion.service.security.ISecurityService;

@Component("token")
public class TokenInterceptor implements HandlerInterceptor {

	@Autowired
	private ISecurityService securityService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String url = request.getRequestURL().toString();
		String httpMethod = request.getMethod();
		
		String token = request.getHeader("Authorization");
		
		
		if(url.contains("swagger") || url.contains("error")) {
			
			return true;
		}
		
		if(url.contains("especialista/v1/user") && httpMethod.equals("POST") ) {
			return true;
		}
		
		if(url.contains("/security/v1/loginUser") && httpMethod.equals("GET") ) {
			return true;
		}
		
		
		checkIsValidToken(token);
		
		return true;
		
	}
	
	
	/**
	 * @param token
	 */
	private void checkIsValidToken(String token) {
		
		boolean status = securityService.isValidToken(token);
		
		if(!status) {
			
			throw new TokenException("Error en token");
		}
		
	}
}
