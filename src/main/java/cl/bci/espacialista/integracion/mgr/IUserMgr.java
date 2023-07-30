package cl.bci.espacialista.integracion.mgr;

import org.springframework.validation.BindingResult;

import cl.bci.espacialista.integracion.dto.RequestUser;
import cl.bci.espacialista.integracion.dto.ResponseGeneric;
import cl.bci.espacialista.integracion.dto.ResponseUser;
import cl.bci.espacialista.integracion.errors.EmailExistException;
import cl.bci.espacialista.integracion.errors.GenericException;

public interface IUserMgr {

	ResponseUser createUser(RequestUser requestUser, BindingResult errors) throws EmailExistException, GenericException;

	ResponseGeneric deleteUser(String idUser, String token);

}
