package cl.bci.espacialista.integracion.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import lombok.Data;

@Entity
@Table(name = "UsersPhone")
@Data
public class UsersPhoneEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String phoneNumber;
	private String cityCode;
	private String countryCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser")
	private UsersEntity user;
	

}
