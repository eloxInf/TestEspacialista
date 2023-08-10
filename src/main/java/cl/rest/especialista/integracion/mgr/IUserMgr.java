package cl.rest.especialista.integracion.mgr;

import org.springframework.validation.BindingResult;

import cl.rest.especialista.integracion.dto.RequestUpdateUser;
import cl.rest.especialista.integracion.dto.RequestUser;
import cl.rest.especialista.integracion.dto.ResponseCreateUser;
import cl.rest.especialista.integracion.dto.ResponseGeneric;
import cl.rest.especialista.integracion.dto.ResponseListUser;
import cl.rest.especialista.integracion.dto.UserDto;
import cl.rest.especialista.integracion.errors.EmailExistException;
import cl.rest.especialista.integracion.errors.GenericException;

public interface IUserMgr {

	ResponseCreateUser createUser(RequestUser requestUser, BindingResult errors) throws EmailExistException, GenericException;

	ResponseGeneric deleteUser(String idUser);

	ResponseListUser getAllUser();

	UserDto getOneUser(String idUser);

	ResponseGeneric updateUser(RequestUpdateUser userUpdate, BindingResult errors);

}
