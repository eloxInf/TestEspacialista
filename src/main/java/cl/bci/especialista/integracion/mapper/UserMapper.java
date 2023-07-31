package cl.bci.especialista.integracion.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import cl.bci.especialista.integracion.dto.PhoneDto;
import cl.bci.especialista.integracion.dto.RequestUser;
import cl.bci.especialista.integracion.dto.ResponseCreateUser;
import cl.bci.especialista.integracion.dto.UserDto;
import cl.bci.especialista.integracion.entity.UsersEntity;
import cl.bci.especialista.integracion.entity.UsersPhoneEntity;

/**
 * @author avenegas
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

	
	@Mapping(source = "password", target = "pass")
	@Mapping(source = "email", target = "email")
	UsersEntity requestUserToUsersEntity(RequestUser requestUser);
	
	
	@Mapping(source = "number", target = "phoneNumber")
	@Mapping(source = "citycode", target = "cityCode")
	@Mapping(source = "contrycode", target = "countryCode")
	UsersPhoneEntity phoneDtoToUsersPhoneEntity(PhoneDto sourceObject);
	

	
	@Mapping(source = "phoneNumber", target = "number")
	@Mapping(source = "cityCode", target = "contrycode")
	@Mapping(source = "countryCode", target = "citycode")
	@Mapping(source = "id", target = "id")
	PhoneDto map(UsersPhoneEntity sourceObject);
	
	List<PhoneDto> listUsersPhoneEntityToListPhoneDto(List<UsersPhoneEntity> soruce);
	
    List<UserDto> listUsersEntityToListUserDto(List<UsersEntity> listUser);
    
    UserDto usersEntityTouserDto(UsersEntity userSoruce);
	
	ResponseCreateUser usersEntityToResponseUser(UsersEntity usersEntity);

}
