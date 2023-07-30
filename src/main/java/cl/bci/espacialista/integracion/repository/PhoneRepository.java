package cl.bci.espacialista.integracion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.bci.espacialista.integracion.entity.UsersPhoneEntity;

@Repository
public interface PhoneRepository extends JpaRepository<UsersPhoneEntity, Integer> {

	
	
}
