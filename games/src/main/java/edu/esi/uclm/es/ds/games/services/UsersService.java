package edu.esi.uclm.es.ds.games.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.esi.uclm.es.ds.games.dao.UserDAO;
import edu.esi.uclm.es.ds.games.entities.User;

@Service
public class UsersService {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private EmailService emailservice;

	private HashMap<String, User> users = new HashMap<>();
	
	public void register(String name, String email, String pwd) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPwd(pwd);
		
		this.userDAO.save(user);
		this.emailservice.sendConfirmationEmail(user);
	}

	public User login(String name, String pwd) {
		User user = this.userDAO.findByName(name);
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas.");
		}

		String pwdEncripted = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		if (!user.getPwd().equals(pwdEncripted)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas.");
		}
		return user;
	}

	public void addMatches(String userName, Integer matches) {
		User user = this.userDAO.findByName(userName);
		Integer paidMatches = user.getPartidas();
		user.setPartidas(paidMatches + matches);
		this.userDAO.save(user);
	}

	public HashMap<String, User> getUsers() {
		return users;
	}
	public void addUser(String token, User user) {
		users.put(token, user);
	}
	public void removeUser(String token) {
		users.remove(token);
	}
	public User getUser(String token) {
		return users.get(token);
	}
}
