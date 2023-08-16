package cl.rest.especialista.integracion.conf;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.security.auth.message.config.AuthConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/*
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		//return httpSecurity.build();
	
		return httpSecurity
				.csrf().disable() // Activarlo en caso de usarlo con formularios con personas.
				.authorizeHttpRequests()
					.antMatchers(HttpMethod.POST, "/especialista/v1/user").permitAll()
					.antMatchers(HttpMethod.GET, "/security/v1/loginUser").permitAll()
					.anyRequest().authenticated()
				.and()
				.formLogin()
				.and()
				.build();
				
	}*/
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		//return httpSecurity.build();
	
		return httpSecurity
				.csrf().disable() // Activarlo en caso de usarlo con formularios con personas.
				.authorizeHttpRequests( auth ->{
					auth.antMatchers(HttpMethod.POST, "/especialista/v1/user").permitAll();
					auth.antMatchers(HttpMethod.GET, "/security/v1/loginUser").permitAll();
					auth.anyRequest().authenticated();
				})
				.formLogin()
					.successHandler(successHandeler())
				.and()
				.build();
		
				
	}
	
	
	public AuthenticationSuccessHandler successHandeler() {
		return ((request, response, authentication) -> {
			response.sendRedirect("/swagger-ui.html");
		});
				
	}
}
