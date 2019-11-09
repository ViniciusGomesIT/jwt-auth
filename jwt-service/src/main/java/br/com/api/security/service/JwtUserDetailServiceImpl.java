package br.com.api.security.service;

import static java.util.Objects.nonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.api.entity.User;
import br.com.api.security.factory.JwtUserFactory;
import br.com.api.service.UserService;

@Service
public class JwtUserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = this.userService.findByEmail(email);
		
		if ( nonNull(user) ) {
			return JwtUserFactory.create(user);
		} else {
			throw new UsernameNotFoundException(String.format("User with username %s not found", email));
		}
	}
}
