package edu.esi.uclm.es.ds.games.domain;

import java.util.concurrent.ConcurrentHashMap;

import edu.esi.uclm.es.ds.games.entities.User;

public class WaitingRoom {
	
	private ConcurrentHashMap<String, Match> matches;
	
	public WaitingRoom() {
		this.matches = new ConcurrentHashMap<>();
	}

	public Match findMatch(String juego, User user) {
		Match match = this.matches.get(juego);
		if (match==null) {
			match = new Match();
			match.addPlayer(user);
			this.matches.put(juego, match);
		}else {
			match.addPlayer(user);
		}
		
		return match;
	}

}
