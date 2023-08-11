package cl.rest.especialista.integracion.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		return httpSecurity
				.csrf().disable() // Activarlo en caso de usarlo con formularios con personas.
				.authorizeHttpRequests()
					.antMatchers(HttpMethod.POST, "/security/v1/loginUser").permitAll()
					.antMatchers(HttpMethod.POST, "/especialista/v1/users").permitAll()
					.anyRequest().authenticated()
				.and()
				.formLogin()
				.and()
				.build();		
	}
}
