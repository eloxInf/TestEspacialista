package cl.rest.especialista.integracion.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author avenegas
 *
 */
@Data
@Builder
public class PhoneUpdateDto {
	private String id;
    private String number;
    private String citycode;
    private String contrycode;

}
