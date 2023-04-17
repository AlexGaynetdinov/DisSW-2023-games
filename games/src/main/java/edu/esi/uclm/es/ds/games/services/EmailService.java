package edu.esi.uclm.es.ds.games.services;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.esi.uclm.es.ds.games.entities.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class EmailService {

	public void sendConfirmationEmail(User user) {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
				MediaType mediaType = MediaType.parse("application/json");
				
				JSONObject jsoBoy = new JSONObject().
						put("name", "Juegos, S.A.").
						put("email", "ismael.roman@alu.uclm.es");

				JSONObject jsoTo = new JSONObject().
						put("email", user.getEmail()).
						put("name", user.getName());
				
				JSONArray jsaTo = new JSONArray().put(jsoTo);
				
				JSONObject jsoBody = new JSONObject();
				jsoBody.put("sender", jsoBoy);
				jsoBody.put("to", jsaTo);
				jsoBody.put("subject", "Bienvenido a los juegos del hambre");
				
				jsoBody.put("htmlContent", "Por favor confirma tu cuenta");
				
				RequestBody body = RequestBody.create(mediaType, jsoBody.toString());
				Request request = new Request.Builder()
				  .url("https://api.sendinblue.com/v3/smtp/email")
				  .method("POST", body)
				  .addHeader("accept", "application/json")
				  .addHeader("api-key", "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")
				  .addHeader("content-type", "application/json")
				  .build();
				
				try {
					Response response = client.newCall(request).execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

}
