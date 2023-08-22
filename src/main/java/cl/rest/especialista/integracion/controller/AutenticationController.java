package cl.rest.especialista.integracion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * @author avenegas
 *
 */
@RestController
@Api(tags = "APIS De Seguridad")
@RequestMapping(value = "/security")
public class AutenticationController {
	
	/*
	@Autowired
	private ISecurity security;
	
	@GetMapping("/v1/loginUser")
    public String generateToken(@RequestParam("email") String username, @RequestParam("pass") String pass) {
        return security.loginUser(username, pass);
    }
    */
	
}
