package cl.rest.especialista.integracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import cl.rest.especialista.integracion.service.IUserServices;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableWebMvc
@SpringBootApplication
public class EspecialistaintegracionApplication {

	@Autowired
	private IUserServices userService;
	
	public static void main(String[] args) {
		

		SpringApplication.run(EspecialistaintegracionApplication.class, args);
	}

}
