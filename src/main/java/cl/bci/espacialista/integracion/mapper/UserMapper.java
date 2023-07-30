package cl.bci.espacialista.integracion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import cl.bci.espacialista.integracion.dto.PhoneDto;
import cl.bci.espacialista.integracion.dto.RequestUser;
import cl.bci.espacialista.integracion.dto.ResponseUser;
import cl.bci.espacialista.integracion.entity.UsersEntity;
import cl.bci.espacialista.integracion.entity.UsersPhoneEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

	@Mapping(source = "number", target = "phoneNumber")
	@Mapping(source = "contrycode", target = "cityCode")
	@Mapping(source = "citycode", target = "countryCode")
	UsersPhoneEntity map(PhoneDto sourceObject);

	@Mapping(source = "password", target = "pass")
	@Mapping(source = "email", target = "email")
	UsersEntity requestUserToUsersEntity(RequestUser requestUser);
	
	
	
	ResponseUser usersEntityToResponseUser(UsersEntity usersEntity);

}
