package cl.rest.especialista.integracion.service;

import java.util.Date;

import cl.rest.especialista.integracion.dto.RequestUpdateUser;
import cl.rest.especialista.integracion.dto.RequestUser;
import cl.rest.especialista.integracion.dto.ResponseCreateUser;
import cl.rest.especialista.integracion.dto.ResponseGeneric;
import cl.rest.especialista.integracion.dto.ResponseListUser;
import cl.rest.especialista.integracion.dto.UserDto;
import cl.rest.especialista.integracion.errors.EmailExistException;


public interface IUserServices {

	ResponseCreateUser createUser(RequestUser userData) throws EmailExistException;

	ResponseGeneric deleteUser(String idUser);

	Boolean checkEmailAndPass(String user, String pass);

	Boolean updateLastLogin(String idUser, String token, Date loginDate);

	ResponseListUser getAllUser();

	UserDto getOneUser(String idUser);

	ResponseGeneric updateUser(RequestUpdateUser userUpdate);

	void createAdminUser();

}
