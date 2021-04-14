package mx.restfulapp.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.restfulapp.model.User;
import mx.restfulapp.repo.UserRepo;

@RestController
@PropertySource(ignoreResourceNotFound = true, value = "classpath:other.properties")
public class RestMainController implements ErrorController {

	@Value("${restapi.sk}")
	private String sk;

	@Autowired
	private UserRepo user_db;

	@PostMapping("/auth")
	public void authorize(HttpRequest request) {
	}

	@PostMapping("/admin/adduser")
	public Map<String, String> addUser(HttpServletRequest request) throws Exception {
		InputStream body = request.getInputStream();
		HashMap<String, String> response = new HashMap<>();
		if(body == null) {
			response.put("msg", "nullbody");
			return response;
		}
		TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
		};
		HashMap<String, String> map = new ObjectMapper().readValue(body, typeRef);
		
		if (map.get("sk") == null) {
			response.put("msg", "skunknown");
			return response;
		}
		if (!map.get("sk").equals(this.sk)) {
			response.put("msg", "skunknown");
			return response;
		}
		if (map.get("username") == null || map.get("password") == null) {
			response.put("msg", "incompleteparameters");
			return response;
		}

		User user = new User();
		user.setUsername(map.get("username"));
		user.setPassword(new BCryptPasswordEncoder().encode(map.get("password")));
		if (user_db.existsByUsername(user.getUsername())) {
			response.put("msg", "usernameexists");
			return response;
		}
		System.out.println(user.getPassword());
		user_db.save(user);
		response.put("ok", "success");
		return response;
	}

	@GetMapping("/op/hello")
	public String hello_world() {
		return "Hola mundo!";
	}

	@RequestMapping(value = "/error")
	public String error() {
		return "Acceso prohibido: debes autenticarte!";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
