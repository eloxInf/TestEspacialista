package cl.bci.espacialista.integracion.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "Users")
@Data
public class UsersEntity {
	@Id
	private String idUser;
	private String name;
	private String email;
	private Date created;
	private Date modified;
	private Date lastLogin;
	private String token;
	private String pass;
	private Boolean isActive;
	
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersPhoneEntity> phones;
}
