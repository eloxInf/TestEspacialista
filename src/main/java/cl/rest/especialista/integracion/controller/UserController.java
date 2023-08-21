package cl.rest.especialista.integracion.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.rest.especialista.integracion.dto.RequestUpdateUser;
import cl.rest.especialista.integracion.dto.RequestUser;
import cl.rest.especialista.integracion.dto.ResponseCreateUser;
import cl.rest.especialista.integracion.dto.ResponseGeneric;
import cl.rest.especialista.integracion.dto.ResponseListUser;
import cl.rest.especialista.integracion.dto.UserDto;
import cl.rest.especialista.integracion.mgr.IUserMgr;
import io.swagger.annotations.Api;

/**
 * @author avenegas
 *
 */
@RestController
@Api(tags = "Apis de Usuario")
@RequestMapping(value = "especialista/v1")
public class UserController {

	@Autowired
	private IUserMgr userMrg;

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

	@PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseCreateUser> createUser(@Valid @RequestBody RequestUser userData,
			BindingResult errors) {

		ResponseCreateUser response = userMrg.createUser(userData, errors);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value = "/users")
	public ResponseEntity<ResponseListUser> getAllUser(@RequestHeader("Authorization") String token) {

		ResponseListUser response = userMrg.getAllUser();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value = "/user/{idUser}")
	public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token, @PathVariable String idUser) {

		UserDto response = userMrg.getOneUser(idUser);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/user/{idUser}")
	public ResponseEntity<ResponseGeneric> deleteUser(@RequestHeader("Authorization") String token,
			@PathVariable String idUser) {

		ResponseGeneric response = userMrg.deleteUser(idUser);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(value = "/user")
	public ResponseEntity<ResponseGeneric> updateUser(
			@RequestHeader("Authorization") @RequestBody RequestUpdateUser userUpdate, String token,
			BindingResult errors) {

		ResponseGeneric response = userMrg.updateUser(userUpdate, errors);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

}
