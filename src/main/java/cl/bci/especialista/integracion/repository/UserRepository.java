package cl.bci.especialista.integracion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.bci.especialista.integracion.entity.UsersEntity;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, String> {

	Optional<UsersEntity> findByEmail(String email);
	
	Boolean existsByEmailAndPass(String email, String pass);
	
	Boolean existsByidUser(String idUser);
		
	
	
	
}
