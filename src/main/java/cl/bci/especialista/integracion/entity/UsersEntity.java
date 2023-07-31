package cl.bci.especialista.integracion.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author avenegas
 *
 */
@Entity
@Table(name = "tb_Users")
@Data
public class UsersEntity {
	@Id
	@Column(name ="id_user")
	private String idUser;
	
	@Column(name ="name")
	private String name;
	
	@Column(name ="email")
	private String email;
	
	@Column(name ="created")
	private Date created;
	
	@Column(name ="modified")
	private Date modified;
	
	@Column(name ="lastLogin")
	private Date lastLogin;
	
	@Column(name ="token")
	private String token;
	
	@Column(name ="pass")
	private String pass;
	
	@Column(name ="isActive")
	private Boolean isActive;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsersPhoneEntity> phones;
}
