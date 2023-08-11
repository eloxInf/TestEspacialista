package cl.rest.especialista.integracion.mgr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import cl.rest.especialista.integracion.dto.RequestUpdateUser;
import cl.rest.especialista.integracion.dto.RequestUser;
import cl.rest.especialista.integracion.dto.ResponseCreateUser;
import cl.rest.especialista.integracion.dto.ResponseGeneric;
import cl.rest.especialista.integracion.dto.ResponseListUser;
import cl.rest.especialista.integracion.dto.UserDto;
import cl.rest.especialista.integracion.errors.EmailExistException;
import cl.rest.especialista.integracion.errors.GenericException;
import cl.rest.especialista.integracion.errors.RequestDataException;
import cl.rest.especialista.integracion.service.IUserServices;
import cl.rest.especialista.integracion.service.UserService;
import cl.rest.especialista.integracion.util.ErrorUtil;

/**
 * @author avenegas
 *
 */
@Component
public class UserMgr implements IUserMgr {
	
	protected static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private IUserServices userServices;
	
	/*
	@Autowired
	private ValuesFromYmlUtil valuesFromYmlUtil;
	*/

	/**
	 * Crea usuario.
	 */
	@Override
	public ResponseCreateUser createUser(RequestUser requestUser, BindingResult errors) throws EmailExistException, GenericException {

		validateError(errors);
		return userServices.createUser(requestUser);
		
	}
	
	
	/**
	 * Obtiene todos los usuarios.
	 */
	@Override
	public ResponseListUser getAllUser() {
		return userServices.getAllUser();
	}
	
	/**
	 * Obtiene un usuario.
	 */
	@Override
	public UserDto getOneUser(String idUser) {
		return userServices.getOneUser(idUser);
	}
	
	
	/**
	 * Elimina un usuario
	 */
	@Override
	public ResponseGeneric deleteUser(String idUser) {

		return userServices.deleteUser(idUser);
		
	}
	

	/**
	 * Actualiza un usuario.
	 */
	@Override
	public ResponseGeneric updateUser(RequestUpdateUser userUpdate, BindingResult errors) {
		
		validateError(errors);	
		return userServices.updateUser(userUpdate);
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
