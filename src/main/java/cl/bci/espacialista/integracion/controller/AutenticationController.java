package cl.bci.espacialista.integracion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.bci.espacialista.integracion.service.security.ISecurityService;
import io.swagger.annotations.Api;

@RestController
@Api(tags = "APIS De Seguridad")
@RequestMapping(value = "/test")
public class AutenticationController {
	
	@Autowired
	private ISecurityService securityService;
	
    @PostMapping("/generate-token")
    public String generateToken(@RequestParam("username") String username, @RequestParam("pass") String pass) {
        return securityService.generateToken(username, pass);
    }
	
}
