package edu.esi.uclm.es.ds.games.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import edu.esi.uclm.es.ds.games.entities.User;
import edu.esi.uclm.es.ds.games.ws.HWSession;
import edu.esi.uclm.es.ds.games.ws.Manager;

public class Match {

	private String id;
	private boolean ready;
	private List <User> players;
	private Board board;

	public Match() {
		this.id = UUID.randomUUID().toString();
		this.players = new ArrayList<>();
		this.board = new Board();
	}

	public String getId() {
		return this.id;
	}

	public boolean isReady() {
		return this.ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
		this.buildBoards();

	}

	private void buildBoards() {
		//Already built in the constructor

	}

	public void addPlayer(User user) {
		this.players.add(user);
		if (this.players.size()==2)
			this.setReady(true);

	}

	public List<String> getPlayers(){
		//Obtain a list of names from the list of players
		return this.players.stream().map(User::getName).collect(Collectors.toList());
	}

	public Board getBoards() {
		return board;
	}


	public void notifyStart() {
		for (User player : players) {
			HWSession hwSession = Manager.get().getSessionByUserId(player.getId());
			WebSocketSession wsSession = hwSession.getWebsocketSession();
			JSONObject jso = new JSONObject().
					put("type", "MATCH STARTED").
					put("matchId", this.id);
			TextMessage message = new TextMessage(jso.toString());
			try {
				wsSession.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
