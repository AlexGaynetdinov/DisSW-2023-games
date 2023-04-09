package edu.esi.uclm.es.ds.games.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.esi.uclm.es.ds.games.domain.Match;
import edu.esi.uclm.es.ds.games.services.GamesService;

@RestController
@RequestMapping("games")
@CrossOrigin(origins = "*")
class GameController {
	
	@Autowired
	private GamesService gamesService;
	@GetMapping("/requestGame")
	public Match requestGame(@RequestParam String juego, @RequestParam String player) {
		if (!juego.equals("nm"))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el juego");
		
		return this.gamesService.requestGame(juego, player);
		
	}
}
