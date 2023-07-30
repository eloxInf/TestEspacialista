package cl.bci.espacialista.integracion.mgr;

import org.springframework.validation.BindingResult;

import cl.bci.espacialista.integracion.dto.RequestUser;
import cl.bci.espacialista.integracion.dto.ResponseGeneric;
import cl.bci.espacialista.integracion.dto.ResponseListUser;
import cl.bci.espacialista.integracion.dto.UserDto;
import cl.bci.espacialista.integracion.dto.ResponseCreateUser;
import cl.bci.espacialista.integracion.errors.EmailExistException;
import cl.bci.espacialista.integracion.errors.GenericException;

public interface IUserMgr {

	ResponseCreateUser createUser(RequestUser requestUser, BindingResult errors) throws EmailExistException, GenericException;

	ResponseGeneric deleteUser(String idUser, String token);

	ResponseListUser getAllUser(String token);

	UserDto getOneUser(String token, String idUser);

}
