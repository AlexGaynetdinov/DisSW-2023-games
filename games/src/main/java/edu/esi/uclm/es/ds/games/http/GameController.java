package edu.esi.uclm.es.ds.games.http;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.games.domain.Match;
import edu.uclm.esi.ds.games.services.GamesService;
import edu.uclm.esi.ds.games.services.UsersService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("games")
@CrossOrigin("*")
public class GamesController {

	@Autowired
	private GamesService gamesService;
	@Autowired
	private UsersService userService;

	@GetMapping("/requestGame")
	public Match request_game(@RequestParam String player) {
		if (userService.getUser(player) == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no ha iniciado sesión.");
		}
		return this.gamesService.request_game(player);
	}
}
