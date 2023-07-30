package cl.bci.espacialista.integracion.mgr;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import cl.bci.espacialista.integracion.dto.RequestUser;
import cl.bci.espacialista.integracion.dto.ResponseGeneric;
import cl.bci.espacialista.integracion.dto.ResponseListUser;
import cl.bci.espacialista.integracion.dto.UserDto;
import cl.bci.espacialista.integracion.dto.ResponseCreateUser;
import cl.bci.espacialista.integracion.errors.EmailExistException;
import cl.bci.espacialista.integracion.errors.GenericException;
import cl.bci.espacialista.integracion.errors.RequestDataException;
import cl.bci.espacialista.integracion.errors.TokenException;
import cl.bci.espacialista.integracion.service.IUserServices;
import cl.bci.espacialista.integracion.service.UserService;
import cl.bci.espacialista.integracion.service.security.ISecurityService;
import cl.bci.espacialista.integracion.util.CommonUtil;
import cl.bci.espacialista.integracion.util.ErrorUtil;
import cl.bci.espacialista.integracion.util.ValuesFromYmlUtil;

@Component
public class UserMgr implements IUserMgr {
	
	protected static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private IUserServices userServices;
	
	@Autowired
	private ValuesFromYmlUtil valuesFromYmlUtil;
	
	
	@Autowired
	private ISecurityService securityService;
	
	@Override
	public ResponseCreateUser createUser(RequestUser requestUser, BindingResult errors) throws EmailExistException, GenericException {

		validRequestUser(requestUser, errors);
		return userServices.createUser(requestUser);
		
	}
	
	
	@Override
	public ResponseListUser getAllUser(String token) {
		checkIsValidToken(token);	
		return userServices.getAllUser();
	}
	
	@Override
	public UserDto getOneUser(String token, String idUser) {
		checkIsValidToken(token);	
		return userServices.getOneUser(idUser);
	}
	
	
	@Override
	public ResponseGeneric deleteUser(String idUser, String token) {
		
		checkIsValidToken(token);	
		return userServices.deleteUser(idUser);
		
	}
	
	
	private void validRequestUser(RequestUser requestUser, BindingResult errors) {
		
		String errorsDetail = ErrorUtil.getDetailError(errors);
		String expresionEmail = valuesFromYmlUtil.getEmailExpresion();
		Boolean validEmail = CommonUtil.validateRegexPattern(requestUser.getEmail(), expresionEmail);
		
		if(!validEmail) {
			throw new RequestDataException("Correo no valido");
		}
		
		if(!errorsDetail.isEmpty()) {	
			throw new RequestDataException(errorsDetail);
		}
	}
	
	private void checkIsValidToken(String token) {
		
		boolean status = securityService.isValidToken(token);
		
		if(!status) {
			
			throw new TokenException("Error en token");
		}
		
		
	}
	
	
	
}
