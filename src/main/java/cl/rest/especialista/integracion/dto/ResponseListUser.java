package cl.rest.especialista.integracion.dto;

import java.util.List;

import lombok.Data;

/**
 * @author avenegas
 *
 */
@Data
public class ResponseListUser {
	
	private List<UserDto> userData;

}