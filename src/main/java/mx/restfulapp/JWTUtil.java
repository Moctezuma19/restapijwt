package mx.restfulapp;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;

public class JWTUtil {
	
	static void addAuthentication(HttpServletResponse res, String username) {
		Signer signer = HMACSigner.newSHA256Signer("too many secrets");
		JWT jwt = new JWT().setIssuer("RESTAPP")
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setSubject(username)
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(15));
		res.addHeader("Authorization", "Bearer " + JWT.getEncoder().encode(jwt, signer));
		
	}
	
	static Authentication getAuthentication(HttpServletRequest req) {
		String header = req.getHeader("Authorization");
		if(header == null) {
			return null;
		} 
		Verifier verifier = HMACVerifier.newVerifier("too many secrets");
		JWT jwt2 = JWT.getDecoder().decode(header.substring(7), verifier);
		String username = jwt2.subject;
		return (username != null)?new UsernamePasswordAuthenticationToken(username,null,java.util.List.copyOf(null)):null;
	}

}
