package cl.bci.especialista.integracion.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import cl.bci.especialista.integracion.dto.PhoneDto;
import cl.bci.especialista.integracion.dto.PhoneUpdateDto;
import cl.bci.especialista.integracion.dto.RequestUpdateUser;
import cl.bci.especialista.integracion.dto.RequestUser;
import cl.bci.especialista.integracion.dto.ResponseCreateUser;
import cl.bci.especialista.integracion.dto.ResponseGeneric;
import cl.bci.especialista.integracion.dto.ResponseListUser;
import cl.bci.especialista.integracion.dto.UserDto;
import cl.bci.especialista.integracion.entity.UsersEntity;
import cl.bci.especialista.integracion.entity.UsersPhoneEntity;
import cl.bci.especialista.integracion.errors.EmailExistException;
import cl.bci.especialista.integracion.errors.GenericException;
import cl.bci.especialista.integracion.errors.UserNotFoundException;
import cl.bci.especialista.integracion.mapper.UserMapper;
import cl.bci.especialista.integracion.repository.PhoneRepository;
import cl.bci.especialista.integracion.repository.UserRepository;
import cl.bci.especialista.integracion.service.security.ISecurityService;
import cl.bci.especialista.integracion.util.CommonUtil;

/**
 * @author avenegas
 *
 */
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

	/**
	 * Crea el usuario
	 */
	@Override
	@Transactional
	public ResponseCreateUser createUser(RequestUser userData) throws EmailExistException, GenericException {

		if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
			throw new EmailExistException("Correo ya existe en otro usuario");

		}

		try {

			UsersEntity usersEntity = setDataCreateUser(userData);

			List<UsersPhoneEntity> listPhone = setDataPhone(userData.getPhones(), usersEntity);
			usersEntity.setPhones(listPhone);

			UsersEntity userSave = userRepository.save(usersEntity);
			ResponseCreateUser responseUser = userMapper.usersEntityToResponseUser(userSave);

			return responseUser;

		} catch (Exception e) {
			log.error("[UserService]-[createUser] error : " + e.getMessage());
			throw new GenericException("Internal Error");

		}

	}

	/**
	 * valida si existe mail y pass para un usuario.
	 */
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

	/**
	 * Actualiza el ultimo login y token
	 */
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

	/**
	 * @param idUser
	 * @return
	 */
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

	/**
	 * @param email
	 * @return
	 */
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

	/**
	 * Elimina el usuario.
	 */
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

	/**
	 * Obtiene todos los usuarios.
	 */
	@Override
	public ResponseListUser getAllUser() {
		ResponseListUser response = new ResponseListUser();

		List<UsersEntity> listUser = userRepository.findAll();

		List<UserDto> listUserDto = new ArrayList<>();
		for (UsersEntity user : listUser) {

			UserDto userDto = userMapper.usersEntityTouserDto(user);
			List<PhoneDto> listphone = userMapper.listUsersPhoneEntityToListPhoneDto(user.getPhones());
			userDto.setPhons(listphone);

			listUserDto.add(userDto);
		}

		response.setUserData(listUserDto);

		return response;
	}

	/**
	 * Obtiene un usuario en particular.
	 */
	@Override
	public UserDto getOneUser(String idUser) {
		UsersEntity userFind = findUserById(idUser);

		UserDto userDto = userMapper.usersEntityTouserDto(userFind);
		List<PhoneDto> listphone = userMapper.listUsersPhoneEntityToListPhoneDto(userFind.getPhones());

		userDto.setPhons(listphone);

		return userDto;

	}
	

	/**
	 * Actualiza un usuario y sus telefonos.
	 */
	@Override
	@Transactional
	public ResponseGeneric updateUser(RequestUpdateUser userRequest) {

		ResponseGeneric response = new ResponseGeneric();
		response.setMessage("ok");

		Optional<UsersEntity> userFind = userRepository.findById(userRequest.getIdUser());
		

		if (!userFind.isPresent()) {
			throw new UserNotFoundException("Usuario no existe");

		}
		
		// TODO validar que el mail no exista en otro usuario.

		try {

			UsersEntity userToUpdate = userFind.get();

			List<UsersPhoneEntity> actualPhone = userToUpdate.getPhones();
			List<PhoneUpdateDto> requestPhone = userRequest.getPhons();


			newPhone(actualPhone, requestPhone, userToUpdate);
			updatePhone(actualPhone, requestPhone);
			deletePhone(actualPhone, requestPhone);
			
			userToUpdate.setEmail(userRequest.getEmail());
			userToUpdate.setIsActive(userRequest.isActive());

			userToUpdate = userRepository.save(userToUpdate);
			userRepository.flush();




		} catch (Exception e) {
			log.error("[UserService]-[updateUser] error : ", e.getMessage());
			throw new GenericException("Internal Error");

		}

		return response;
	}


	/**
	 * @param userData
	 * @return
	 */
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

	/**
	 * @param listPhoneDto
	 * @param userSave
	 * @return
	 */
	private List<UsersPhoneEntity> setDataPhone(ArrayList<PhoneDto> listPhoneDto, UsersEntity userSave) {
		List<UsersPhoneEntity> listPhone = new ArrayList<>();
		for (PhoneDto phone : listPhoneDto) {
			UsersPhoneEntity phoneEnty = userMapper.phoneDtoToUsersPhoneEntity(phone);
			phoneEnty.setUser(userSave);
			listPhone.add(phoneEnty);
		}

		return listPhone;
	}

	
	/**
	 * @param actualPhone
	 * @param requestPhone
	 * @param userToUpdate
	 */
	private void newPhone(List<UsersPhoneEntity> actualPhone, List<PhoneUpdateDto> requestPhone, UsersEntity userToUpdate) {

		List<PhoneUpdateDto> newPhoneList = requestPhone.stream()
				.filter(x -> (x.getId() == null || x.getId().isEmpty())).collect(Collectors.toList());

		for (PhoneUpdateDto newpPhoneDto : newPhoneList) {

			UsersPhoneEntity newPhoneInsert = new UsersPhoneEntity();
			newPhoneInsert.setCityCode(newpPhoneDto.getCitycode());
			newPhoneInsert.setCountryCode(newpPhoneDto.getContrycode());
			newPhoneInsert.setPhoneNumber(newpPhoneDto.getNumber());
			newPhoneInsert.setUser(userToUpdate);

			phoneRepository.save(newPhoneInsert);

		}
		
	}
	
	/**
	 * @param actualPhone
	 * @param requestPhone
	 */
	private void updatePhone(List<UsersPhoneEntity> actualPhone, List<PhoneUpdateDto> requestPhone)  {
		
		List<UsersPhoneEntity> updateCurrentPhone = actualPhone.stream()
				.filter(x -> findPhoneinRequest(requestPhone, x.getId() + "")).collect(Collectors.toList());

		for (UsersPhoneEntity phonUpdate : updateCurrentPhone) {
			PhoneUpdateDto phoneRequest = requestPhone.stream()
					.filter(x -> x.getId().equals(phonUpdate.getId() + "")).findFirst().get();
			phonUpdate.setCityCode(phoneRequest.getCitycode());
			phonUpdate.setCountryCode(phoneRequest.getContrycode());
			phonUpdate.setPhoneNumber(phoneRequest.getNumber());
			phoneRepository.save(phonUpdate);

		}
	}
	
	/**
	 * @param actualPhone
	 * @param requestPhone
	 */
	private void deletePhone(List<UsersPhoneEntity> actualPhone, List<PhoneUpdateDto> requestPhone) {
		List<UsersPhoneEntity> deletePhone = actualPhone.stream()
				.filter(x -> !findPhoneinRequest(requestPhone, x.getId() + "")).collect(Collectors.toList());
		
		for(UsersPhoneEntity phoneDelete : deletePhone) {
			phoneRepository.delete(phoneDelete);
		}
	}

	/**
	 * @param updatePhone
	 * @param id
	 * @return
	 */
	private boolean findPhoneinRequest(List<PhoneUpdateDto> updatePhone, String id) {

		Optional<PhoneUpdateDto> findElement = updatePhone.stream().filter(x -> x.getId().equals(id)).findAny();
		return findElement.isPresent();

	}

}
