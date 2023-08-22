package cl.rest.especialista.integracion.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import cl.rest.especialista.integracion.errors.RequestDataException;
import cl.rest.especialista.integracion.service.IUserServices;
import cl.rest.especialista.integracion.util.ErrorUtil;
import io.swagger.annotations.Api;

/**
 * @author avenegas
 *
 */
@RestController
@Api(tags = "Apis de Usuario")
@RequestMapping(value = "especialista/v1")
public class UserController {

	//@Autowired
	//private IUserMgr userMrg;
	
	@Autowired
	private IUserServices userServices;

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
		
		validateError(errors);
		ResponseCreateUser response = userServices.createUser(userData);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value = "/users")
	public ResponseEntity<ResponseListUser> getAllUser() {

		ResponseListUser response = userServices.getAllUser();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value = "/user/{idUser}")
	public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token, @PathVariable String idUser) {

		UserDto response = userServices.getOneUser(idUser);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/user/{idUser}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseGeneric> deleteUser(
			@PathVariable String idUser) {

		ResponseGeneric response = userServices.deleteUser(idUser);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(value = "/user")
	public ResponseEntity<ResponseGeneric> updateUser(
			 @RequestBody RequestUpdateUser userUpdate, 
			BindingResult errors) {
		
		validateError(errors);
		ResponseGeneric response = userServices.updateUser(userUpdate);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
	/**
	 * @param requestUser
	 * @param errors
	 */
	private void validateError(BindingResult errors) {	
		String errorsDetail = ErrorUtil.getDetailError(errors);

		if(!errorsDetail.isEmpty()) {	
			throw new RequestDataException(errorsDetail);
		}
	}

}
