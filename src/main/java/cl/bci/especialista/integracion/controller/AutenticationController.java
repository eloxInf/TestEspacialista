package cl.bci.especialista.integracion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.bci.especialista.integracion.mgr.security.ISecurity;
import io.swagger.annotations.Api;

/**
 * @author avenegas
 *
 */
@RestController
@Api(tags = "APIS De Seguridad")
@RequestMapping(value = "/security")
public class AutenticationController {
	
	
	@Autowired
	private ISecurity security;
	
	@GetMapping("/v1/loginUser")
    public String generateToken(@RequestParam("email") String username, @RequestParam("pass") String pass) {
        return security.loginUser(username, pass);
    }
	
}
