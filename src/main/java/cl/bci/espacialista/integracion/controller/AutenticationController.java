package cl.bci.espacialista.integracion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.bci.espacialista.integracion.mgr.security.ISecurity;
import cl.bci.espacialista.integracion.service.security.ISecurityService;
import io.swagger.annotations.Api;

@RestController
@Api(tags = "APIS De Seguridad")
@RequestMapping(value = "/security")
public class AutenticationController {
	
	
	@Autowired
	private ISecurity security;
	
    @PostMapping("/loginUser")
    public String generateToken(@RequestParam("email") String username, @RequestParam("pass") String pass) {
        return security.loginUser(username, pass);
    }
	
}
