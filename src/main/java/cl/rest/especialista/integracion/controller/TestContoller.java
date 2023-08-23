package cl.rest.especialista.integracion.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RequestMapping(value = "especialista/v1")
@RestController
@Api(tags = "Apis de Usuario")
public class TestContoller {

	/*
	@Autowired
	private SessionRegistry sessionRegistry;

	@GetMapping("/session")
	public ResponseEntity<?> getSession() {

		String sessionId = "";
		User userObject = null;

		List<Object> sessionsList = sessionRegistry.getAllPrincipals();

		for (Object session : sessionsList) {
			if (session instanceof User) {
				userObject = (User) session;
			}

			List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(sessionsList, false);
			
			for (SessionInformation sessionInformation : sessionInformations) {	
				sessionId = sessionInformation.getSessionId();		
			}
			
		}
		
		Map<String, Object> responseData = new HashMap<>();
		
		responseData.put("sessionId", sessionId);
		responseData.put("sessionUser", userObject);

		return ResponseEntity.ok(responseData);

	}
	*/
	
}
