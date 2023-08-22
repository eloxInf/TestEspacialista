package cl.rest.especialista.integracion.service;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import cl.rest.especialista.integracion.dto.PhoneUpdateDto;
import cl.rest.especialista.integracion.dto.RequestUpdateUser;
import cl.rest.especialista.integracion.dto.RequestUser;
import cl.rest.especialista.integracion.dto.ResponseCreateUser;
import cl.rest.especialista.integracion.dto.ResponseGeneric;
import cl.rest.especialista.integracion.dto.ResponseListUser;
import cl.rest.especialista.integracion.dto.UserDto;
import cl.rest.especialista.integracion.entity.UsersEntity;
import cl.rest.especialista.integracion.entity.UsersPhoneEntity;
import cl.rest.especialista.integracion.errors.EmailExistException;
import cl.rest.especialista.integracion.errors.GenericException;
import cl.rest.especialista.integracion.errors.UserNotFoundException;
import cl.rest.especialista.integracion.mapper.UserMapper;
import cl.rest.especialista.integracion.repository.PhoneRepository;
import cl.rest.especialista.integracion.repository.UserRepository;
import cl.rest.especialista.integracion.util.CommonUtil;

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

	/*
	@Autowired
	private ISecurityService securityService;
	*/
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Crea el usuario
	 */
	@Override
	@Transactional
	public ResponseCreateUser createUser(RequestUser requestUser) throws EmailExistException, GenericException {

		userRepository.findByEmail(requestUser.getEmail()).ifPresent(user -> {
		    throw new EmailExistException("Correo ya existe en otro usuario");
		});

		try {
			
			UsersEntity usersEntity = setDataCreateUser(requestUser);
			List<UsersPhoneEntity> listPhone = userMapper.listPhoneDtoToUsersListPhoneEntity(requestUser.getPhones(), usersEntity);
			usersEntity.setPhones(listPhone);
			usersEntity.setRoles(userMapper.listRoleToEntity(requestUser.getRoles()));
			UsersEntity userSave = userRepository.save(usersEntity);
			return userMapper.usersEntityToResponseCreateUser(userSave);

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

		boolean exist = false;

		try {
			exist = userRepository.existsByEmailAndPass(user, pass).isPresent();
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

		
		Optional<UsersEntity> userFind = userRepository.findById(idUser);
		userFind.orElseThrow(() -> new UserNotFoundException("Usuario no existe"));

		try {
			userFind.get().setToken(idUser);
			userFind.get().setLastLogin(loginDate);
			userRepository.save(userFind.get());

		} catch (Exception e) {
			log.error("[UserService]-[updateLastLogin] error : ", e.getMessage());
			throw new GenericException("Internal Error");

		}

		return true;

	}



	/**
	 * Elimina el usuario.
	 */
	@Override
	public ResponseGeneric deleteUser(String idUser) {

		Optional<UsersEntity> userDelete = userRepository.findById(idUser);
		userDelete.orElseThrow(() -> new UserNotFoundException("Usuario no existe"));

		try {

			userRepository.delete(userDelete.get());
			return ResponseGeneric.builder().message("Ok").build();
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

		List<UsersEntity> listUser = userRepository.findAll();
		List<UserDto> listUserDto = userMapper.listUsersEntityToListUserDto(listUser);
		
		return (ResponseListUser.builder().userData(listUserDto).build());

	}

	/**
	 * Obtiene un usuario en particular.
	 */
	@Override
	public UserDto getOneUser(String idUser) {
		
		Optional<UsersEntity> userFind =  userRepository.findById(idUser);	
		userFind.orElseThrow(()-> new UserNotFoundException("Usuario no existe"));
		
		UserDto userDto = userMapper.userBdToUserDto(userFind.get());
		return userDto;

	}
	

	

	/**
	 * Actualiza un usuario y sus telefonos.
	 */
	@Override
	@Transactional
	public ResponseGeneric updateUser(RequestUpdateUser userRequest) {
		
		Optional<UsersEntity> userFind = userRepository.findById(userRequest.getIdUser());	
		userFind.orElseThrow(() -> new UserNotFoundException("Usuario no existe"));

		try {

			UsersEntity userToUpdate = userFind.get();

			userToUpdate.setName(userRequest.getName());
			userToUpdate.setEmail(userRequest.getEmail());
			userToUpdate.setIsActive(userRequest.isActive());
			userToUpdate = userRepository.save(userToUpdate);
			
			updateUserPhone(userToUpdate, userRequest );

		} catch (Exception e) {
			log.error("[UserService]-[updateUser] error : ", e.getMessage());
			throw new GenericException("Internal Error");

		}

		return ResponseGeneric.builder().message("ok").build();
	}
	
	public void getUserByEmail() {
		
	}
	
	
	/**
	 * @param userToUpdate
	 * @param userRequest
	 */
	private void updateUserPhone(UsersEntity userToUpdate, RequestUpdateUser userRequest) {
		List<UsersPhoneEntity> actualPhone = userToUpdate.getPhones();
		List<PhoneUpdateDto> requestPhone = userRequest.getPhons();
		
		newPhone(actualPhone, requestPhone, userToUpdate);
		updatePhone(actualPhone, requestPhone);
		deletePhone(actualPhone, requestPhone);
	}


	/**
	 * @param userData
	 * @return
	 */
	private UsersEntity setDataCreateUser(RequestUser requestUser) {


		Map<String, Object> propertyUser = new HashMap<String, Object>();
		propertyUser.put("typeUser", "SA");

		//String token = securityService.createToken(propertyUser, requestUser.getName());
		
		Date dateNew = new Date();
		
		UsersEntity userToCreate = UsersEntity.builder()
		.name(requestUser.getName())
		.pass(passwordEncoder.encode(requestUser.getPassword()))
		.email(requestUser.getEmail())
		.idUser(CommonUtil.generateUUID())
		.token("")
		.modified(dateNew)
		.created(dateNew)
		.lastLogin(dateNew)
		.isActive(true)
		.build();

		return userToCreate;
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
