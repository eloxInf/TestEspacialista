package cl.bci.especialista.integracion.service;

import java.util.Date;

import cl.bci.especialista.integracion.dto.RequestUpdateUser;
import cl.bci.especialista.integracion.dto.RequestUser;
import cl.bci.especialista.integracion.dto.ResponseCreateUser;
import cl.bci.especialista.integracion.dto.ResponseGeneric;
import cl.bci.especialista.integracion.dto.ResponseListUser;
import cl.bci.especialista.integracion.dto.UserDto;
import cl.bci.especialista.integracion.errors.EmailExistException;


public interface IUserServices {

	ResponseCreateUser createUser(RequestUser userData) throws EmailExistException;

	ResponseGeneric deleteUser(String idUser);

	Boolean checkEmailAndPass(String user, String pass);

	Boolean updateLastLogin(String idUser, String token, Date loginDate);

	ResponseListUser getAllUser();

	UserDto getOneUser(String idUser);

	ResponseGeneric updateUser(RequestUpdateUser userUpdate);

}
