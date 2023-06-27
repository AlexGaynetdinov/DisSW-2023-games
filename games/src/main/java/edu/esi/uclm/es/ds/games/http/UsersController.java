package edu.esi.uclm.es.ds.games.http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.esi.uclm.es.ds.games.entities.User;
import edu.esi.uclm.es.ds.games.services.UsersService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
class UsersController {
	
	@Autowired
	private UsersService usersService;
	
	@PostMapping("/register")
	public void register(@RequestBody Map<String, Object> info) {
		String name = info.get("name").toString();
		String email = info.get("email").toString();
		String pwd1 = info.get("pwd1").toString();
		String pwd2 = info.get("pwd2").toString();

		if (name.contains(" ") || email.contains(" ") || pwd1.contains(" ") || pwd2.contains(" "))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		
		if (!pwd1.equals(pwd2))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");
		
		try {
			this.usersService.register(name, email, pwd1);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}
	
	@PutMapping("/login")
	public Map<String, Object> login(@RequestBody Map<String, Object> info) {
		String name = info.get("name").toString();
		String pwd = info.get("pwd").toString();

		if (name.contains(" ") || pwd.contains(" "))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);

		try {
			User user = this.usersService.login(name, pwd);
			String token = UUID.randomUUID().toString();
			this.usersService.addUser(token, user);
			Map<String, Object> alt = new HashMap<>();
			alt.put("tokenIsmael", token);
			
			return alt;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}
}
