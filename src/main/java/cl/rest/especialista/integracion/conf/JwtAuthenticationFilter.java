package cl.rest.especialista.integracion.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private JwtUtils jwtUtils;
	
	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = "";
		String password ="";
		
		try {		
			 Map<String, String> authData  = new ObjectMapper().readValue(request.getInputStream(), Map.class);	
			 
			 username = authData.get("username");
		     password = authData.get("password");	
		     	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		return getAuthenticationManager().authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		User user = (User)authResult.getPrincipal();
		String token = jwtUtils.generateAccessToken(user.getUsername());
		
		response.addHeader("Authorization", token);
		
		Map<String, Object> httpResponseMap = new HashMap<>();
		
		httpResponseMap.put("Token", token);
		httpResponseMap.put("Messagge", "Autorizacion Correcta");
		httpResponseMap.put("UserName", user.getUsername());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponseMap));
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().flush();
		

		super.successfulAuthentication(request, response, chain, authResult);
	}

	
}
