package edu.esi.uclm.es.ds.games.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.esi.uclm.es.ds.games.entities.Token;

public interface TokenDAO extends JpaRepository<Token, String>{

}
