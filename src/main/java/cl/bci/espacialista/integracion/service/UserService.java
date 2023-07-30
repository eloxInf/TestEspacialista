package cl.bci.espacialista.integracion.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import cl.bci.espacialista.integracion.dto.PhoneDto;
import cl.bci.espacialista.integracion.dto.RequestUser;
import cl.bci.espacialista.integracion.dto.ResponseGeneric;
import cl.bci.espacialista.integracion.dto.ResponseListUser;
import cl.bci.espacialista.integracion.dto.UserDto;
import cl.bci.espacialista.integracion.dto.ResponseCreateUser;
import cl.bci.espacialista.integracion.entity.UsersEntity;
import cl.bci.espacialista.integracion.entity.UsersPhoneEntity;
import cl.bci.espacialista.integracion.errors.EmailExistException;
import cl.bci.espacialista.integracion.errors.GenericException;
import cl.bci.espacialista.integracion.errors.UserNotFoundException;
import cl.bci.espacialista.integracion.mapper.UserMapper;
import cl.bci.espacialista.integracion.repository.PhoneRepository;
import cl.bci.espacialista.integracion.repository.UserRepository;
import cl.bci.espacialista.integracion.service.security.ISecurityService;
import cl.bci.espacialista.integracion.util.CommonUtil;

@Service
public class UserService implements IUserServices {

	protected static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private PhoneRepository phoneRepository;
	
	

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ISecurityService securityService;

	@Override
	@Transactional
	public ResponseCreateUser createUser(RequestUser userData) throws EmailExistException, GenericException {

		if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
			throw new EmailExistException("Correo ya existe");

		}

		try {

			UsersEntity usersEntity = setDataCreateUser(userData);	
			
			List<UsersPhoneEntity> listPhone = setDataPhone(userData.getPhones(), usersEntity);
			usersEntity.setPhones(listPhone);

			UsersEntity userSave = userRepository.save(usersEntity);
			ResponseCreateUser responseUser = userMapper.usersEntityToResponseUser(userSave);
			

			return responseUser;

		} catch (Exception e) {
			log.error("[UserService]-[createUser] error : ", e.getCause());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new GenericException("Internal Error");

		}

	}

	@Override
	public Boolean checkEmailAndPass(String user, String pass) {

		Boolean exist = false;

		try {
			exist = userRepository.existsByEmailAndPass(user, pass);
		} catch (Exception e) {
			log.error("[UserService]-[checkUserPass] error : ", e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new GenericException("Internal Error");

		}
		return exist;
	}

	@Override
	public Boolean updateLastLogin(String idUser, String token, Date loginDate) {
		UsersEntity user;

		user = findUserByEmail(idUser);

		try {
			user.setToken(idUser);
			user.setLastLogin(loginDate);
			userRepository.save(user);
			
		} catch (Exception e) {
			log.error("[UserService]-[updateLastLogin] error : ", e.getMessage());
			throw new GenericException("Internal Error");

		}

		return true;

	}
	
	private UsersEntity findUserById(String idUser) {
		Optional<UsersEntity> userFind;
		try {

			userFind = userRepository.findById(idUser);

			if (!userFind.isPresent())
				throw new UserNotFoundException("Usuario no existe");

		} catch (Exception e) {
			log.error("[UserService]-[findUserByEmail] error : ", e.getMessage());
			throw new GenericException("Internal Error");

		}

		return userFind.get();

	}

	private UsersEntity findUserByEmail(String email) {
		Optional<UsersEntity> userFind;
		try {

			userFind = userRepository.findByEmail(email);

			if (!userFind.isPresent())
				throw new UserNotFoundException("Usuario no existe");

		} catch (Exception e) {
			log.error("[UserService]-[findUserByEmail] error : ", e.getMessage());
			throw new GenericException("Internal Error");

		}

		return userFind.get();

	}

	@Override
	public ResponseGeneric deleteUser(String idUser) {
		
		Optional<UsersEntity> userDelete = userRepository.findById(idUser);
		
		if (!userDelete.isPresent()) {
			throw new UserNotFoundException("Usuario no existe");

		}
		try {

			ResponseGeneric response = new ResponseGeneric();
			response.setMessage("ok");

			userRepository.delete(userDelete.get());
			return response;
		} catch (Exception e) {
			log.error("[UserService]-[deleteUser] error : ", e.getMessage());
			throw new GenericException("Internal Error");

		}

	}
	

	@Override
	public ResponseListUser getAllUser() {
		ResponseListUser response = new ResponseListUser();
		
		List<UsersEntity> listUser = userRepository.findAll();
		
		List<UserDto> listUserDto = new ArrayList<>();
		for (UsersEntity user :listUser) {
			
			UserDto userDto = userMapper.usersEntityTouserDto(user);
			List<PhoneDto> listphone = userMapper.listUsersPhoneEntityToListPhoneDto(user.getPhones());
			userDto.setPhons(listphone);	
			
			listUserDto.add(userDto);
		}

		response.setUserData(listUserDto);

		return response;
	}
	
	@Override
	public UserDto getOneUser(String idUser) {
		UsersEntity  userFind =	findUserById(idUser);
		
		UserDto userDto = userMapper.usersEntityTouserDto(userFind);
		List<PhoneDto> listphone = userMapper.listUsersPhoneEntityToListPhoneDto(userFind.getPhones());
		
		userDto.setPhons(listphone);
		
		return userDto;
		
	}
	

	@Override
	public void getUser() {

	}

	@Override
	public void updateUser() {

	}

	private UsersEntity setDataCreateUser(RequestUser userData) {
		
		Map<String, Object> propertyUser = new HashMap<String, Object>();
		propertyUser.put("typeUser", "SA");
		
		String token = securityService.createToken(propertyUser, userData.getName());

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
	
	private List<UsersPhoneEntity> setDataPhone(ArrayList<PhoneDto> listPhoneDto, UsersEntity userSave) {
		List<UsersPhoneEntity> listPhone = new ArrayList<>();
		for(PhoneDto phone : listPhoneDto) {	
			UsersPhoneEntity phoneEnty = userMapper.phoneDtoToUsersPhoneEntity(phone);
			phoneEnty.setUser(userSave);
			listPhone.add(phoneEnty);
		}
		
		return listPhone;
	}

}
