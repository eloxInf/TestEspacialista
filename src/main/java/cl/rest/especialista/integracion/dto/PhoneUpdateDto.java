package cl.rest.especialista.integracion.dto;

import lombok.Data;

/**
 * @author avenegas
 *
 */
@Data
public class PhoneUpdateDto {
	private String id;
    private String number;
    private String citycode;
    private String contrycode;

}
