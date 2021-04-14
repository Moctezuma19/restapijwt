package mx.restfulapp.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mx.restfulapp.repo.UserRepo;

@Service
public class UserService implements UserDetailsService{
	@Autowired
	private UserRepo user_db;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		mx.restfulapp.model.User u = user_db.findByUsername(username);
		UserDetails userDetails;
		GrantedAuthority authority = new SimpleGrantedAuthority(u.getUsername());
		userDetails = (UserDetails) new User(u.getUsername(), u.getPassword(),
				Arrays.asList(authority));
		return userDetails;
	}
	
}
