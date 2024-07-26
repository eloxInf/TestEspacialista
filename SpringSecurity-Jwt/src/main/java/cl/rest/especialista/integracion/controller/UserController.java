package cl.rest.especialista.integracion.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.rest.especialista.integracion.dto.RequestUpdateUser;
import cl.rest.especialista.integracion.dto.RequestUser;
import cl.rest.especialista.integracion.dto.ResponseCreateUser;
import cl.rest.especialista.integracion.dto.ResponseGeneric;
import cl.rest.especialista.integracion.dto.ResponseListUser;
import cl.rest.especialista.integracion.dto.UserDto;
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

	@Autowired
	private IUserServices userServices;
	
	@PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseCreateUser> createUser(@Valid @RequestBody RequestUser userData,
			BindingResult errors) {
		
		ErrorUtil.validateError(errors);
		return new ResponseEntity<>(userServices.createUser(userData), HttpStatus.CREATED);
	}

	@GetMapping(value = "/users")
	//@PreAuthorize("hasAnyRole('ADMIN','EDITOR', 'USER')")
	public ResponseEntity<ResponseListUser> getAllUser() {
		return new ResponseEntity<>(userServices.getAllUser(), HttpStatus.CREATED);
	}

	@GetMapping(value = "/user/{idUser}")
	//@PreAuthorize("hasRole('ADMIN','EDITOR', 'USER')")
	public ResponseEntity<UserDto> getUser(@PathVariable String idUser) {
		return new ResponseEntity<>(userServices.getOneUser(idUser), HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/user/{idUser}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseGeneric> deleteUser(
			@PathVariable String idUser) {
		
		return new ResponseEntity<>(userServices.deleteUser(idUser), HttpStatus.CREATED);
	}

	@PutMapping(value = "/user")
	//@PreAuthorize("hasRole('ADMIN','EDITOR')")
	public ResponseEntity<ResponseGeneric> updateUser(
			 @RequestBody RequestUpdateUser userUpdate, 
			BindingResult errors) {
		return new ResponseEntity<>(userServices.updateUser(userUpdate), HttpStatus.CREATED);
	}


}
