package cl.rest.especialista.integracion.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author avenegas
 *
 */
@Entity
@Table(name = "tb_Users_Phone")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersPhoneEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_user_phone")
	private int id;
	
	@Column(name="phoneNumber", nullable = false, length = 9)
	private String phoneNumber;
	
	@Column(name="cityCode", nullable = false, length = 2)
	private String cityCode;
	
	@Column(name="countryCode", nullable = false, length = 2)
	private String countryCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", referencedColumnName = "id_user", insertable = true, updatable = true)
    private UsersEntity user;
	

}
