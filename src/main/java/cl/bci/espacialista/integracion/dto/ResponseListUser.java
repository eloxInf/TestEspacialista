package cl.bci.espacialista.integracion.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResponseListUser {
	
	private List<UserDto> userData;

}
