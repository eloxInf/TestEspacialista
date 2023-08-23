package cl.rest.especialista.integracion.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cl.rest.especialista.integracion.dto.PhoneDto;
import cl.rest.especialista.integracion.dto.PhoneUpdateDto;
import cl.rest.especialista.integracion.dto.ResponseCreateUser;
import cl.rest.especialista.integracion.dto.UserDto;
import cl.rest.especialista.integracion.entity.ERole;
import cl.rest.especialista.integracion.entity.RoleEntity;
import cl.rest.especialista.integracion.entity.UsersEntity;
import cl.rest.especialista.integracion.entity.UsersPhoneEntity;

@Component
public class UserMapper {

	public List<RoleEntity> listRoleToEntity(List<String> roles) {

		return roles.stream().map(role -> RoleEntity.builder().name(ERole.valueOf(role)).build())
				.collect(Collectors.toList());
	}

	public List<UsersPhoneEntity> listPhoneDtoToUsersListPhoneEntity(List<PhoneDto> list, UsersEntity userSave) {
		List<UsersPhoneEntity> listPhoneEntity = list.stream()
				.map(phone -> UsersPhoneEntity.builder().phoneNumber(phone.getNumber()).user(userSave)
						.cityCode(phone.getCitycode()).countryCode(phone.getContrycode()).build())
				.collect(Collectors.toList());

		return listPhoneEntity;
	}

	public List<PhoneDto> phoneBdToPhoneDto(List<UsersPhoneEntity> phones) {

		List<PhoneDto> listPhoneResponse = phones.stream()
				.map(phoneBd -> PhoneDto.builder().id(phoneBd.getId() + "").number(phoneBd.getPhoneNumber())
						.citycode(phoneBd.getCityCode()).contrycode(phoneBd.getCountryCode()).build())
				.collect(Collectors.toList());

		return listPhoneResponse;

	}

	public UsersPhoneEntity phoneUpdateDtoToUsersPhoneEntity(PhoneUpdateDto userDto, UsersEntity usersEntity) {

		return UsersPhoneEntity.builder().cityCode(userDto.getCitycode()).countryCode(userDto.getContrycode())
				.phoneNumber(userDto.getNumber()).user(usersEntity).build();
	}

	public UserDto userBdToUserDto(UsersEntity userBd) {

		UserDto userDto = UserDto.builder().idUser(userBd.getIdUser()).name(userBd.getName()).email(userBd.getEmail())
				.lastLogin(userBd.getLastLogin()).modified(userBd.getModified()).created(userBd.getCreated())
				.isActive(userBd.getIsActive()).token(userBd.getToken()).phons(phoneBdToPhoneDto(userBd.getPhones()))
				.build();

		return userDto;

	}

	public List<UserDto> listUsersEntityToListUserDto(List<UsersEntity> listUser) {

		List<UserDto> listUserDto = listUser.stream()
				.map(userBd -> UserDto.builder().idUser(userBd.getIdUser()).name(userBd.getName())
						.email(userBd.getEmail()).lastLogin(userBd.getLastLogin()).modified(userBd.getModified())
						.created(userBd.getCreated()).isActive(userBd.getIsActive()).token(userBd.getToken())
						.phons(phoneBdToPhoneDto(userBd.getPhones())).build())
				.collect(Collectors.toList());

		return listUserDto;
	}

	public ResponseCreateUser usersEntityToResponseCreateUser(UsersEntity userSave) {

		List<String> roles = userSave.getRoles().stream().map(RoleEntity::getName).map(ERole::toString)
				.collect(Collectors.toList());

		ResponseCreateUser responseUser = ResponseCreateUser.builder().idUser(userSave.getIdUser())
				.isActive(userSave.getIsActive()).lastLogin(userSave.getLastLogin()).modified(userSave.getModified())
				.created(userSave.getCreated()).token(userSave.getToken()).roles(roles).build();

		return responseUser;

	}
}
