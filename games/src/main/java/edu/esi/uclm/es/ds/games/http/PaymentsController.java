package edu.esi.uclm.es.ds.games.http;

import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import edu.esi.uclm.es.ds.games.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentsController {
	@Autowired
	private UsersService userService;
	
	// Mapa<tokenIsmael, userSecretStripe>
	private HashMap<String, String> tokenMap = new HashMap<>();
	// Mapa<tokenIsmael, matches>
	private HashMap<String, Integer> tokenMatches = new HashMap<>();
	
	static {
		Stripe.apiKey = "sk_test_51Mo0YCL0kXbyHZbWSCfolJKWyPS8ltu97mCG0qGR9HUVRfPIBwLt0YkogetpOdGoaN2tlNtnu0laC19XbjUjfK6s00sumLVhAa";
	}
	
	@RequestMapping("/prepay")
	public String prepay(HttpServletRequest request, @RequestParam double amount) {
		try {
			String header = request.getHeader("tokenIsmael");
			System.out.println(header);
			if (header == null)
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,
						"El usuario debe haber iniciado sesión previamente.");
			if (amount != 10 && amount != 20)
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"Solo se pueden comprar 10 o 20 partidas a la vez.");

			long total = (long) (amount*100);

			PaymentIntentCreateParams params = new PaymentIntentCreateParams
					.Builder()
					.setCurrency("eur")
					.setAmount(total)
					.build();
				
			PaymentIntent intent = PaymentIntent.create(params);
			JSONObject jso = new JSONObject(intent.toJson());
			String clientSecret = jso.getString("client_secret");
			
			// Cada transacción de stripe y cada cantidad se queda guardada a un token
			tokenMap.put(header, clientSecret);
			tokenMatches.put(header, (int) amount);
			
			return clientSecret;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/confirm")
	public void confirm(HttpServletRequest request) {
		// Recogemos el token del usuario que no cambia
		String token = request.getHeader("tokenIsmael");
		String player = request.getHeader("player");
		int amount = tokenMatches.get(token);
		// Si el token no esta almacenado da error
		if (token.equals("") || token == null
				|| tokenMap.get(token).equals("") || tokenMap.get(token) == null
				|| player.equals("") || player == null
				|| amount == 0) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		
		this.userService.addMatches(player, amount);
	}
}
