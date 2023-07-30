package cl.bci.espacialista.integracion.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import cl.bci.espacialista.integracion.dto.RequestUser;
import cl.bci.espacialista.integracion.dto.ResponseGeneric;
import cl.bci.espacialista.integracion.dto.ResponseUser;
import cl.bci.espacialista.integracion.entity.UsersEntity;
import cl.bci.espacialista.integracion.errors.EmailExistException;
import cl.bci.espacialista.integracion.errors.GenericException;
import cl.bci.espacialista.integracion.errors.UserNotFoundException;
import cl.bci.espacialista.integracion.mapper.UserMapper;
import cl.bci.espacialista.integracion.repository.UserRepository;
import cl.bci.espacialista.integracion.service.security.ISecurityService;
import cl.bci.espacialista.integracion.util.CommonUtil;

@Service
public class UserService implements IUserServices {

	protected static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ISecurityService securityService;

	@Override
	@Transactional
	public ResponseUser createUser(RequestUser userData) throws EmailExistException, GenericException {

		if (userRepository.existsByEmail(userData.getEmail())) {
			throw new EmailExistException("Correo ya existe");

		}

		try {

			UsersEntity usersEntity = setDataUser(userData);
			UsersEntity userSave = userRepository.save(usersEntity);
			ResponseUser responseUser = userMapper.usersEntityToResponseUser(userSave);

			return responseUser;

		} catch (Exception e) {
			log.error("[UserService]-[UserService] error : ", e.getCause());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new GenericException("Internal Error");

		}

	}

	@Override
	public ResponseGeneric deleteUser(String idUser) {

		if (userRepository.existsById(idUser)) {
			throw new UserNotFoundException("Usuario no existe");

		}
		try {

			ResponseGeneric response = new ResponseGeneric();
			response.setMessage("ok");
			
			userRepository.deleteById(idUser);
			return response;
		} catch (Exception e) {
			log.error("[UserService]-[deleteUser] error : ", e.getMessage());
			throw new GenericException("Internal Error");

		}

	}

	@Override
	public void getUser() {

	}

	@Override
	public void updateUser() {

	}

	private UsersEntity setDataUser(RequestUser userData) {
		String token = securityService.generateToken(userData.getName(), userData.getPassword());

		UsersEntity usersEntity = userMapper.requestUserToUsersEntity(userData);

		String id = CommonUtil.generateUUID();
		usersEntity.setIdUser(id);
		usersEntity.setToken(token);

		Date dateNew = new Date();

		usersEntity.setCreated(dateNew);
		usersEntity.setLastLogin(dateNew);
		usersEntity.setModified(dateNew);

		usersEntity.setIsActive(true);

		return usersEntity;
	}

}
