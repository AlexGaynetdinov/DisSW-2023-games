package edu.esi.uclm.es.ds.games.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WSConfigurer implements WebSocketConfigurer {
	@Autowired
	private GamesService gamesService;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new WSGames(gamesService), "/wsGames").setAllowedOrigins("*")
				.addInterceptors(new HttpSessionHandshakeInterceptor());
	}
}
