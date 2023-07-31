package cl.bci.especialista.integracion.dto;

import lombok.Data;

/**
 * @author avenegas
 *
 */
@Data
public class PhoneDto {
	private String id;
    private String number;
    private String citycode;
    private String contrycode;

}
