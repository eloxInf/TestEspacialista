package cl.bci.espacialista.integracion.service;

import java.util.Date;

import cl.bci.espacialista.integracion.dto.RequestUser;
import cl.bci.espacialista.integracion.dto.ResponseGeneric;
import cl.bci.espacialista.integracion.dto.ResponseListUser;
import cl.bci.espacialista.integracion.dto.UserDto;
import cl.bci.espacialista.integracion.dto.ResponseCreateUser;
import cl.bci.espacialista.integracion.errors.EmailExistException;


public interface IUserServices {




	void getUser();

	void updateUser();

	ResponseCreateUser createUser(RequestUser userData) throws EmailExistException;

	ResponseGeneric deleteUser(String idUser);

	Boolean checkEmailAndPass(String user, String pass);

	Boolean updateLastLogin(String idUser, String token, Date loginDate);

	ResponseListUser getAllUser();

	UserDto getOneUser(String idUser);

}
