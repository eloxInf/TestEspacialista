package cl.rest.especialista.integracion.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {
	
	@Autowired
	UserDetailsServiceImpl userServiceImpl;
	
	@Autowired
	JwtAuthorizationFilter jwtAuthorizationFilter;
	
	@Autowired
	JwtUtils jwtUtils;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager autenAuthenticationManager) throws Exception {
		
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
		jwtAuthenticationFilter.setAuthenticationManager(autenAuthenticationManager);
		jwtAuthenticationFilter.setFilterProcessesUrl("/security/v1/login");
		
		return httpSecurity
				.csrf().disable()
				//.authorizeHttpRequests( auth ->{
				//	auth.antMatchers(HttpMethod.POST, "/especialista/v1/user").permitAll();
				//	auth.antMatchers(HttpMethod.GET, "/security/v1/loginUser").permitAll();
				//	auth.anyRequest().authenticated();
				//	})
				.sessionManagement( session -> {
					session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);	
				})
				.addFilter(jwtAuthenticationFilter)
				.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
				//.httpBasic()
				.build();
	}
	

	@Bean
	PasswordEncoder passwordEncoder() {
		//return NoOpPasswordEncoder.getInstance();	
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
		
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userServiceImpl)
				.passwordEncoder(passwordEncoder)
				.and().build();
		
	}
	
	/*
	@Bean
	UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("angelo")
				.password("123456")
				.roles("")
			    .build());
		
		return manager;
		
	}*/
	
	
	/*
	@Bean // Autenticacion Basica
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
	

	
	/*
	@Bean // Autenticacion Basica
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
					.successHandler(successHandeler()) // URL donde va despues de iniciar session
				.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Alwais (Crea una session si no existe ninguna) - IF Requiered (Crea una session solo si es necesario) - NEVER (No crea una session, si existe la reutlizara) - STATLES (No trabaja con session)
					.invalidSessionUrl("/login")
					.maximumSessions(1)
					.expiredUrl("/login") // Tiempo de inactividad 
					.sessionRegistry(sessionRegistry())
				.and()
				.sessionFixation() // Revisar que tipo de vulnerabilidad es la que proteje.
					.migrateSession() // Cuando se detecta un id de session, intenta usar otro en caso de ataque.
					//.none() // No hace nada.
					//.newSession() // Hace lo mismo que lo anterior, pero no copia los datos.
				.and()
				.httpBasic()
				.and()
				.build();
		
				
	}
	
	
	public AuthenticationSuccessHandler successHandeler() {
		return ((request, response, authentication) -> {
			//response.sendRedirect("/swagger-ui.html");
			response.sendRedirect("/especialista/v1/session");
			
		});
				
	}
	*/
	@Bean
	public SessionRegistry sessionRegistry() {
		
		return new SessionRegistryImpl();
	}
	
}
