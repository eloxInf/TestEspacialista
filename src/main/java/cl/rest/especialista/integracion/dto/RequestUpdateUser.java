package cl.rest.especialista.integracion.dto;

import java.util.List;

import lombok.Data;

/**
 * @author avenegas
 *
 */
@Data
public class RequestUpdateUser {

    private String idUser;
    private String name;
    private String email;
    private boolean isActive;
    private List<PhoneUpdateDto> phons;
}
