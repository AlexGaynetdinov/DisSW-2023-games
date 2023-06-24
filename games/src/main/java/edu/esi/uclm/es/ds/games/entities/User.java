package edu.esi.uclm.es.ds.games.entities;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
		name = "users",
		indexes = {
				@Index(columnList = "name", unique = true),
				@Index(columnList = "email", unique = true),
		}
)
public class User {
	@Id @Column(length = 36)
	private String id;
	@Column(length = 100) @NotEmpty
	private String name;
	@Column(length = 140) @NotEmpty
	private String email;
	@Column(length = 255) @NotEmpty
	private String pwd;
	@Column(length = 8) @NotEmpty
	private Integer partidas;
	
	public User() {
		id = UUID.randomUUID().toString();
		partidas = 0;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPwd(String pwd) {
		this.pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
	}
	public void setPartidas(int partidas) {
		this.partidas = partidas;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	public String getEmail() {
		return this.email;
	}
	public String getPwd() {
		return this.pwd;
	}
	public int getPartidas() {
		return this.partidas;
	}
	public String getId() {
		return this.id;
	}
}
