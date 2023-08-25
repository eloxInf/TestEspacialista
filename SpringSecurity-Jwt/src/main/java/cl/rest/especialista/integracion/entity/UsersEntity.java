package cl.rest.especialista.integracion.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@Table(name = "TB_USERS")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {
	@Id
	@Column(name ="id_user")
	private String idUser;
	
	@Column(name ="name", nullable = false, length = 100)
	private String name;
	
	@Column(name ="email" , nullable = false, length = 100)
	private String email;
	
	@Column(name ="created", nullable = false)
	private Date created;
	
	@Column(name ="modified", nullable = false)
	private Date modified;
	
	@Column(name ="lastLogin", nullable = false)
	private Date lastLogin;
	
	@Column(name ="token", nullable = false)
	private String token;
	
	@Column(name ="pass", nullable = false)
	private String pass;
	
	@Column(name ="isActive", nullable = false)
	private Boolean isActive;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UsersPhoneEntity> phones;
	
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.ALL)
	@JoinTable(name = "TB_USER_ROLE", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name= "role_id"))
	private List<RoleEntity> roles;
}
