package edu.uclm.esi.ds.games.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.ds.games.domain.Token;

public interface TokenDAO extends JpaRepository<Token, String>{

}
