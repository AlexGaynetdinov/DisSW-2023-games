package edu.esi.uclm.es.ds.games.http;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentsController {
	
	static {
		Stripe.apiKey = "sk_test_51Mo0YCL0kXbyHZbWSCfolJKWyPS8ltu97mCG0qGR9HUVRfPIBwLt0YkogetpOdGoaN2tlNtnu0laC19XbjUjfK6s00sumLVhAa";
	}
	
	@RequestMapping("/prepay")
	public String prepay(@RequestParam double amount) {
		long total = (long) Math.floor(amount*100);
		
		PaymentIntentCreateParams params = new PaymentIntentCreateParams
				.Builder()
				.setCurrency("eur")
				.setAmount(total)
				.build();
		
		try {
			PaymentIntent intent = PaymentIntent.create(params);
			JSONObject jso = new JSONObject(intent.toJson());
			String clientSecret = jso.getString("client_secret");
			System.out.println(clientSecret);
			//String encripted = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
			
			return clientSecret;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha podido procesar el pago.");
		}
	}
	
	@PostMapping(name = "/paymentOk", consumes = "application/json")
	public void paymentOk(@RequestBody Map<String, String> info) {
		String token = info.get("token");
	}
}