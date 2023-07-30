package cl.bci.espacialista.integracion.service;

import cl.bci.espacialista.integracion.dto.RequestUser;
import cl.bci.espacialista.integracion.dto.ResponseGeneric;
import cl.bci.espacialista.integracion.dto.ResponseUser;
import cl.bci.espacialista.integracion.errors.EmailExistException;


public interface IUserServices {




	void getUser();

	void updateUser();

	ResponseUser createUser(RequestUser userData) throws EmailExistException;

	ResponseGeneric deleteUser(String idUser);

}
