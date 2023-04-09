package edu.esi.uclm.es.ds.games;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.esi.uclm.es.ds.games.dao.UserDAO;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)

public class TestUser {

	@Autowired
	private MockMvc server;
	@Autowired
	private UserDAO userDao;
	
	@Test @Order(1)
	void testRegister() throws Exception {
		this.userDao.deleteAll();
		ResultActions result = this.sendRequest("Pepe", "pepe@pepe.com", "Pepe1234", "Pepe1234");
		result.andExpect(status().isOk()).andReturn();
		
		result = this.sendRequest("Ana", "ana@ana.com", "Ana1234", "Ana123");
		result.andExpect(status().isNotAcceptable()).andReturn();
		
		result = this.sendRequest("Ana", "ana@ana.com", "Ana1234", "Ana1234");
		result.andExpect(status().is(200)).andReturn();
	}


	@Test @Order(2)
	void testLogin() throws Exception {
		ResultActions result1 = this.sendLogin("Pepe", "Pepe1234");
		result1.andExpect(status().is(200)).andReturn();
		
		ResultActions result2 = this.sendLogin("Ana", "*****");
		result2.andExpect(status().is(403)).andReturn();
		
		ResultActions result3 = this.sendLogin("Ana", "Ana1234");
		result3.andExpect(status().is(200)).andReturn();
	}


	private ResultActions sendLogin(String name, String pwd) throws Exception {
		JSONObject jsoUser = new JSONObject().
				put("name", name).
				put("pwd", pwd);
		
		RequestBuilder request = MockMvcRequestBuilders.
				put("/users/login").contentType("application/json").
				content(jsoUser.toString());
		
		ResultActions resultActions = this.server.perform(request);
		return resultActions;
	}


	private ResultActions sendRequest(String name, String email, String pwd1, String pwd2) throws Exception, UnsupportedEncodingException {
		JSONObject jsoUser = new JSONObject().
				put("name", name).
				put("email", email).
				put("pwd1", pwd1).
				put("pwd2", pwd2);
		
		RequestBuilder request = MockMvcRequestBuilders.
				post("/users/register").contentType("application/json").
				content(jsoUser.toString());
		
		ResultActions resultActions = this.server.perform(request);
		return resultActions;
	}
		
}
