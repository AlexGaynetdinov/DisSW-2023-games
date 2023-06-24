package edu.esi.uclm.es.ds.games.services;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import edu.esi.uclm.es.ds.games.domain.Match;
import edu.esi.uclm.es.ds.games.domain.WaitingRoom;
import edu.esi.uclm.es.ds.games.entities.User;

@Service
public class GamesService {

	private WaitingRoom waitingRoom;
	private ConcurrentHashMap<String, Match> matches;
	
	public GamesService() {
		this.waitingRoom = new WaitingRoom();
		System.out.println("CREADO EL GAMES SERVICE");
		this.matches = new ConcurrentHashMap<>();
	}
	
	public Match requestGame(String juego, User user) {
		Match match = this.waitingRoom.findMatch(juego, user);
		if (match.isReady()) {
			this.matches.put(match.getId(), match);
			// Match start notification
			match.notifyStart();
			System.out.println("INICIO DE PARTIDA NOTIFICADO");
		}
		return match;
		//There is an else here
	}

}
