package cl.rest.especialista.integracion.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.rest.especialista.integracion.config.AppConfig;
import cl.rest.especialista.integracion.config.security.JwtUtils;
import cl.rest.especialista.integracion.constant.Constant;
import cl.rest.especialista.integracion.dto.PhoneUpdateDto;
import cl.rest.especialista.integracion.dto.RequestUpdateUser;
import cl.rest.especialista.integracion.dto.RequestUser;
import cl.rest.especialista.integracion.dto.ResponseCreateUser;
import cl.rest.especialista.integracion.dto.ResponseGeneric;
import cl.rest.especialista.integracion.dto.ResponseListUser;
import cl.rest.especialista.integracion.dto.UserDto;
import cl.rest.especialista.integracion.entity.ERole;
import cl.rest.especialista.integracion.entity.RoleEntity;
import cl.rest.especialista.integracion.entity.UsersEntity;
import cl.rest.especialista.integracion.entity.UsersPhoneEntity;
import cl.rest.especialista.integracion.errors.GenericException;
import cl.rest.especialista.integracion.mapper.UserMapper;
import cl.rest.especialista.integracion.repository.PhoneRepository;
import cl.rest.especialista.integracion.repository.UserRepository;
import cl.rest.especialista.integracion.util.CommonUtil;
import cl.rest.especialista.integracion.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author avenegas
 *
 */
@Slf4j
@Service
public class UserService implements IUserServices {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PhoneRepository phoneRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private AppConfig appConfig;

	@Override
	public void createAdminUser() {

		try {

			Date dateNew = new Date();
			List<String> listRole = Arrays.asList("ADMIN", "INVITED", "EDITOR", "USER");

			UsersEntity usersEntity = UsersEntity.builder().idUser(CommonUtil.generateUUID())
					.pass(passwordEncoder.encode("Just21")).name("administrador").created(dateNew).modified(dateNew)
					.email("admin@admin.com").token("").isActive(true).lastLogin(dateNew)
					.roles(listRole.stream().map(role -> RoleEntity.builder().name(ERole.valueOf(role)).build())
							.collect(Collectors.toList()))
					.build();

			userRepository.save(usersEntity);
		} catch (Exception e) {
			log.error("[UserService]-[createAdminUser] error : " + e.getMessage());
			throw new GenericException("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Crea el usuario
	 */
	@Override
	@Transactional
	public ResponseCreateUser createUser(RequestUser requestUser) {

		try {
			
			userRepository.findByEmail(requestUser.getEmail()).ifPresent(user -> {
				throw new GenericException(Constant.EMAIL_EXISTS, HttpStatus.CONFLICT);
			});
			
			boolean isValidRol = requestUser.getRoles().stream().allMatch(ErrorUtil::isRoleValid);
			
			if(!isValidRol) {
				userRepository.findByEmail(requestUser.getEmail()).ifPresent(user -> {
					throw new GenericException(Constant.ROLE_NOT_EXIST , HttpStatus.CONFLICT);
				});
			}
			
			if(!CommonUtil.validateRegexPattern(requestUser.getPassword(), appConfig.getPassRegex())) {
				throw new GenericException(Constant.INVALID_PASSWORD , HttpStatus.BAD_REQUEST);
			}
			
			
			if(!CommonUtil.validateRegexPattern(requestUser.getEmail(), appConfig.getEmailRegex())){
				throw new GenericException(Constant.INVALID_EMAIL , HttpStatus.BAD_REQUEST);
			}
			
			
			Date dateNew = new Date();
			Boolean inicialStatus = true;
			UsersEntity usersEntity = userMapper.requestUserToUsersEntity(requestUser, passwordEncoder.encode(requestUser.getPassword()), jwtUtils.generateAccessToken(requestUser.getEmail()), dateNew, inicialStatus);
			
			
			List<UsersPhoneEntity> listPhone = userMapper.listPhoneDtoToUsersListPhoneEntity(requestUser.getPhones(),
					usersEntity);
			
			usersEntity.setPhones(listPhone);
			usersEntity.setRoles(userMapper.listRoleToEntity(requestUser.getRoles()));
			
			UsersEntity userSave = userRepository.save(usersEntity);
			
			return userMapper.usersEntityToResponseCreateUser(userSave);

		} catch (GenericException e) {
			throw e;

		} catch (Exception e) {
			log.error("[UserService]-[createUser] error : " + e.getMessage());
			throw new GenericException(Constant.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	/**
	 * Actualiza el ultimo login y token
	 */
	@Override
	public Boolean updateLastLogin(String idUser, String token, Date loginDate) {
		try {
			UsersEntity userFind = userRepository.findById(idUser)
					.orElseThrow(() -> new GenericException(Constant.USER_NOT_FOUND ,  HttpStatus.NOT_FOUND));

			userFind.setToken(idUser);
			userFind.setLastLogin(loginDate);
			userRepository.save(userFind);

		} catch (GenericException e) {
			throw e;
		} catch (Exception e) {
			log.error("[UserService]-[updateLastLogin] error : ", e.getMessage());
			throw new GenericException(Constant.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return true;

	}

	/**
	 * Elimina el usuario.
	 */
	@Override
	public ResponseGeneric deleteUser(String idUser) {
		try {
			UsersEntity userDelete = userRepository.findById(idUser)
					.orElseThrow(() -> new GenericException(Constant.USER_NOT_FOUND,  HttpStatus.NOT_FOUND));

			userRepository.delete(userDelete);
			return ResponseGeneric.builder().message(Constant.OK).build();
		} catch (GenericException e) {
			throw e;
		} catch (Exception e) {
			log.error("[UserService]-[deleteUser] error : ", e.getMessage());
			throw new GenericException(Constant.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	/**
	 * Obtiene todos los usuarios.
	 */
	@Override
	public ResponseListUser getAllUser() {
		try {
			List<UsersEntity> listUser = userRepository.findAll();
			List<UserDto> listUserDto = userMapper.listUsersEntityToListUserDto(listUser);

			return (ResponseListUser.builder().userData(listUserDto).build());
		} catch (Exception e) {
			log.error("[UserService]-[getAllUser] error : ", e.getMessage());
			throw new GenericException(Constant.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	/**
	 * Obtiene un usuario en particular.
	 */
	@Override
	public UserDto getOneUser(String idUser) {
		try {
			UsersEntity userFind = userRepository.findById(idUser)
					.orElseThrow(() -> new GenericException(Constant.USER_NOT_FOUND,  HttpStatus.NOT_FOUND));
			UserDto userDto = userMapper.userBdToUserDto(userFind);
			return userDto;
			
		} catch (GenericException e) {
			throw e;

		} catch (Exception e) {
			log.error("[UserService]-[getOneUser] error : ", e.getMessage());
			throw new GenericException(Constant.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Actualiza un usuario y sus telefonos.
	 */
	@Override
	@Transactional
	public ResponseGeneric updateUser(RequestUpdateUser userRequest) {
		try {
			UsersEntity userFind = userRepository.findById(userRequest.getIdUser()).orElseThrow(() -> new GenericException(Constant.USER_NOT_FOUND,  HttpStatus.NOT_FOUND));;

			userFind.setName(userRequest.getName());
			userFind.setEmail(userRequest.getEmail());
			userFind.setIsActive(userRequest.isActive());
			userFind = userRepository.save(userFind);
			
			updateUserPhone(userFind, userRequest);
			
		} catch (GenericException e) {
			throw e;

		} catch (Exception e) {
			log.error("[UserService]-[updateUser] error : ", e.getMessage());
			throw new GenericException(Constant.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return ResponseGeneric.builder().message(Constant.OK).build();
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
	 * @param actualPhone
	 * @param requestPhone
	 * @param userToUpdate
	 */
	private void newPhone(List<UsersPhoneEntity> actualPhone, List<PhoneUpdateDto> requestPhone,
			UsersEntity userToUpdate) {

		requestPhone.stream()
				.filter(x -> (x.getId() == null || x.getId().isEmpty())).forEach(newphone -> {	
					UsersPhoneEntity newPhoneInsert =userMapper.phoneUpdateDtoToUsersPhoneEntity(newphone, userToUpdate);
					phoneRepository.save(newPhoneInsert);					
				});
	}

	/**
	 * @param actualPhone
	 * @param requestPhone
	 */
	private void updatePhone(List<UsersPhoneEntity> actualPhone, List<PhoneUpdateDto> requestPhone) {

		actualPhone.stream()
				.filter(x -> findPhoneinRequest(requestPhone, x.getId() + "")).forEach(phonUpdate -> {	
					PhoneUpdateDto phoneRequest = requestPhone.stream().filter(x -> x.getId().equals(phonUpdate.getId() + ""))
							.findFirst().get();
					
					phonUpdate.setCityCode(phoneRequest.getCitycode());
					phonUpdate.setCountryCode(phoneRequest.getContrycode());
					phonUpdate.setPhoneNumber(phoneRequest.getNumber());
					phoneRepository.save(phonUpdate);	
				
				});

	}

	/**
	 * @param actualPhone
	 * @param requestPhone
	 */
	private void deletePhone(List<UsersPhoneEntity> actualPhone, List<PhoneUpdateDto> requestPhone) {
		
		List<UsersPhoneEntity> actualPhoneDelete = 	actualPhone.stream()
				.filter(x -> !findPhoneinRequest(requestPhone, x.getId() + "")).toList();
		
		phoneRepository.deleteAll(actualPhoneDelete);
	
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
	

	/**
	 * @param userData
	 * @return
	 */
	private UsersEntity setDataCreateUser(RequestUser requestUser) {
	
		Date dateNew = new Date();
		
		Boolean inicialStatus = true;
		
		

		
		/*
		UsersEntity userToCreate = UsersEntity.builder()
				.name(requestUser.getName())
				.pass(passwordEncoder.encode(requestUser.getPassword()))
				.email(requestUser.getEmail())
				.idUser(CommonUtil.generateUUID())
				.token(jwtUtils.generateAccessToken(requestUser.getEmail()))
				.modified(dateNew)
				.created(dateNew)
				.lastLogin(dateNew)
				.isActive(true).build();
						return userToCreate;
*/
		
		return null;

	}

}
