package cl.rest.especialista.integracion.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * @author avenegas
 *
 */
@Data
public class RequestUpdateUser {
	@NotEmpty
    private String idUser;
	@NotEmpty
    private String name;
	@NotEmpty
    private String email;
	@NotEmpty
    private boolean isActive;
	@NotEmpty
    private List<PhoneUpdateDto> phons;
}
