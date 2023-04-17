package edu.esi.uclm.es.ds.games.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Token {
	@Id
	@Column(length = 36)
	private String id;
	private long creationTime;
	private long confirmationtime;
	@ManyToOne
	private User user;
	
	public Token() {
		this.id = UUID.randomUUID().toString();
		this.creationTime = System.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getConfirmationtime() {
		return confirmationtime;
	}

	public void setConfirmationtime(long confirmationtime) {
		this.confirmationtime = confirmationtime;
	}
}
