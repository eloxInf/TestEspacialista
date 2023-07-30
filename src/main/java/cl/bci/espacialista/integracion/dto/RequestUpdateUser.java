package cl.bci.espacialista.integracion.dto;

import java.util.List;

import lombok.Data;

@Data
public class RequestUpdateUser {

    private String idUser;
    private String name;
    private String email;
    private boolean isActive;
    private List<PhoneUpdateDto> phons;
}
