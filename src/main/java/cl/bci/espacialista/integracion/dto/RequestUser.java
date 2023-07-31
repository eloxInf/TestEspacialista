package cl.bci.espacialista.integracion.dto;

import java.util.ArrayList;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * @author avenegas
 *
 */
@Data
public class RequestUser {

	@NotEmpty
    private String name;
	@NotEmpty
    private String email;
	@NotEmpty
    private String password;
	@NotEmpty
    private ArrayList<PhoneDto> phones;
}
