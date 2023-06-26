package edu.uclm.esi.ds.games.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.ds.games.domain.Match;
import edu.uclm.esi.ds.games.services.GamesService;

@Component
public class WSGames extends TextWebSocketHandler {
	
	private GamesService gamesService;

	// 
	private ConcurrentHashMap<String, WebSocketSession> sessionByUser = new ConcurrentHashMap<>();
	private ArrayList<WebSocketSession> sessions = new ArrayList<>();
	
	public WSGames(GamesService service) {
		this.gamesService = service;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception { // OnOpen
		System.out.println();
		this.sessions.add(session);
		// Sacamos el token de la url
		String query = session.getUri().getQuery();
		String userToken = query.substring("tokenIsmael=".length());
		
		// Añadimos la sesion por usuario
		this.sessionByUser.put(userToken, session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception { // OnMessage
		String payload = message.getPayload();
		JSONObject jso = new JSONObject(payload);
		String type = jso.getString("type");
		if (type.equals("MOVEMENT")) {
			JSONObject altJso = this.move(jso);
			this.send(session, altJso);
		} else {
			JSONObject altJso = new JSONObject();
			altJso.put("type", "ERROR");
			altJso.put("message", "Mensaje no reconocido");
			this.send(session, altJso);
		}
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) { // OnMessage
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception { // OnError
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) { // OnClose
		this.sessions.remove(session);
		JSONObject jso = new JSONObject();
		jso.put("type", "BYE");
		jso.put("message", "Un usuario se ha ido");
		this.broadcast(jso);
	}
	
	private void send(WebSocketSession session, JSONObject tv) {
		TextMessage message = new TextMessage(tv.toString());
		try {
			session.sendMessage(message);
		} catch (IOException e) {
			this.sessions.remove(session);
		}
	}

	private JSONObject move(JSONObject jso) throws Exception{
		String idMatch = jso.getString("match");
		String player = jso.getString("player");
		int columna = jso.getInt("column");
		
		Match partida = this.gamesService.makeMove(idMatch, columna, player);
		
		JSONObject altJso = new JSONObject(partida);
		//altJso.put("board", partida.getBoard());

		return altJso;
	}
	
	public void broadcast(JSONObject jso) {
		TextMessage message = new TextMessage(jso.getString("message"));
		for (WebSocketSession client : this.sessions) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						client.sendMessage(message);
					} catch (IOException e) {
						WSGames.this.sessions.remove(client);
					}
				}};
			new Thread(r).start();
		}
	}
}