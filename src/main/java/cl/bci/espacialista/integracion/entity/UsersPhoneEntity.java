package cl.bci.espacialista.integracion.entity;

import javax.persistence.Column;
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
@Table(name = "tb_Users_Phone")
@Data
public class UsersPhoneEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_user_phone")
	private int id;
	
	@Column(name="phoneNumber")
	private String phoneNumber;
	
	@Column(name="cityCode")
	private String cityCode;
	
	@Column(name="countryCode")
	private String countryCode;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "id_user", referencedColumnName = "id_user", insertable = true, updatable = true)
    private UsersEntity user;
	

}
