package edu.esi.uclm.es.ds.games.ws;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WSGames extends TextWebSocketHandler {

	private ArrayList<WebSocketSession> sessions = new ArrayList<>();

	@Override // ese websocket representa al navegador
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		this.sessions.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();// aqui cogemos el texto hola pepe, hola ana...
		JSONObject jso = new JSONObject(payload);
		String type = jso.getString("type");
		if (type.equals("MOVEMENT")) {
			this.move(jso);
		} else if (type.equals("CHAT")) {
			this.chat(jso);
		} else if (type.equals("BROADCAST")) {
			this.broadcast(jso);
		} else {
			this.send(session, "type", "ERROR", "message", "Mensaje no reconocido");

		}

	}

	private void send(WebSocketSession session, String... tv) {
		JSONObject jso = new JSONObject();
		for (int i = 0; i < tv.length; i = i + 2) {
			jso.put("nombre", "Macario");
			jso.put(tv[i], tv[i + 1]);
		}
		TextMessage message = new TextMessage(jso.toString());
		try {
			session.sendMessage(message);
		} catch (IOException e) {
			this.sessions.remove(session);
		}

	}

	private void broadcast(JSONObject jso) {
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

				}
			};
			new Thread(r).start();// hilos no procesos

		}
	}

	private void chat(JSONObject jso) {
		// TODO Auto-generated method stub

	}

	private void move(JSONObject jso) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		this.sessions.remove(session);
		JSONObject jso = new JSONObject();
		jso.put("type", "BYE");
		jso.put("message", "Un usuario se ha ido");
		this.broadcast(jso);
	}
}