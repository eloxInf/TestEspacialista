package cl.bci.especialista.integracion.mgr;

import org.springframework.validation.BindingResult;

import cl.bci.especialista.integracion.dto.RequestUpdateUser;
import cl.bci.especialista.integracion.dto.RequestUser;
import cl.bci.especialista.integracion.dto.ResponseCreateUser;
import cl.bci.especialista.integracion.dto.ResponseGeneric;
import cl.bci.especialista.integracion.dto.ResponseListUser;
import cl.bci.especialista.integracion.dto.UserDto;
import cl.bci.especialista.integracion.errors.EmailExistException;
import cl.bci.especialista.integracion.errors.GenericException;

public interface IUserMgr {

	ResponseCreateUser createUser(RequestUser requestUser, BindingResult errors) throws EmailExistException, GenericException;

	ResponseGeneric deleteUser(String idUser, String token);

	ResponseListUser getAllUser(String token);

	UserDto getOneUser(String token, String idUser);

	ResponseGeneric updateUser(RequestUpdateUser userUpdate, String token);

}
