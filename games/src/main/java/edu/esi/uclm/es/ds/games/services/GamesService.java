package edu.esi.uclm.es.ds.games.services;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.games.domain.WaitingRoom;
import edu.uclm.esi.ds.games.domain.Match;

@Service
public class GamesService {

	private WaitingRoom waitingRoom;
	private ConcurrentHashMap<String, Match> matches;

	public GamesService() {
		System.out.println("***********************\nCREADO EL GAMES SERVICE\n***********************");
		this.matches = new ConcurrentHashMap<>();
		this.waitingRoom = new WaitingRoom();
	}

	public Match request_game(String player) {
		Match match = this.waitingRoom.findMatch(player);
		if (match.isReady() || true) {
			this.matches.put(match.getId(), match);
		}
		return match;
	}

	public Match makeMove(String idMatch, int column, String player) throws Exception{
		this.matches.get(idMatch).move(column, player);
		return this.matches.get(idMatch);
	}
}
