package edu.esi.uclm.es.ds.games.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.esi.uclm.es.ds.games.entities.User;

public interface UserDAO extends JpaRepository<User, String>{

	//JpaRepository with multiple integrated operations (page 102)
	
	User findByName(String name);
	
}
