package edu.esi.uclm.es.ds.games.domain;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class WaitingRoom {
	// notYetFullMatches<idMatch, Match>
	// Partidas con un solo jugador (en espera)
	private ConcurrentHashMap<String, Match> notYetFullMatches;
	
	// matches<idMatch, Match>
	// Partidas que están llenas
	private ConcurrentHashMap<String, Match> fullMatches;

	public WaitingRoom() {
		this.fullMatches = new ConcurrentHashMap<>();
		this.notYetFullMatches = new ConcurrentHashMap<>();
	}

	public Match findMatch(String playerToken) {
		// Esta persona no esta en ninguna partida, tenemos que encontrarle una o crear una nueva
		Enumeration<String> games = notYetFullMatches.keys();
		try {
			// Intentamos tomar una partida de un jugador
			while (true) { // Si no hay ninguna salta una excepcion
				// Sacamos el id de la partida
				String gameId = games.nextElement();
				
				if (gameId.equals("") || gameId == null) {
					throw new Exception();
				}
				
				// Sacamos la partida con el id
				Match match = notYetFullMatches.get(gameId);
				
				// Comprobamos si el jugador que hay en la partida soy yo
				if (match.getPlayers().contains(playerToken)) {
					continue;
				}
				
				match.addPlayer(playerToken);
				
				// Como la partida ya existía entonces había algun jugador dentro, por lo que ahora esta llena
				this.fullMatches.put(gameId, match);
				this.notYetFullMatches.remove(gameId);

				return match;
			}
		} catch(Exception e) {
			// Si no hay partidas con un solo jugador se crea una y se añade al jugador
			Match match = new Match();
			match.addPlayer(playerToken);
			// Como acaba de ser creada, se añade a las partidas no llenas
			this.notYetFullMatches.put(match.getId(), match);

			return match;
		}
	}
}
