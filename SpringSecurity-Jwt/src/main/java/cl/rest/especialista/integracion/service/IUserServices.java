package cl.rest.especialista.integracion.service;

import java.util.Date;

import cl.rest.especialista.integracion.dto.RequestUpdateUser;
import cl.rest.especialista.integracion.dto.RequestUser;
import cl.rest.especialista.integracion.dto.ResponseCreateUser;
import cl.rest.especialista.integracion.dto.ResponseGeneric;
import cl.rest.especialista.integracion.dto.ResponseListUser;
import cl.rest.especialista.integracion.dto.UserDto;


public interface IUserServices {

	ResponseCreateUser createUser(RequestUser userData);

	ResponseGeneric deleteUser(String idUser);

	Boolean updateLastLogin(String idUser, String token, Date loginDate);

	ResponseListUser getAllUser();

	UserDto getOneUser(String idUser);

	ResponseGeneric updateUser(RequestUpdateUser userUpdate);

	void createAdminUser();

}
