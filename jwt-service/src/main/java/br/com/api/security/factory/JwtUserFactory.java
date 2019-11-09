package br.com.api.security.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.api.entity.User;
import br.com.api.enums.ProfileEnum;
import br.com.api.security.model.JwtUser;

public class JwtUserFactory {
	
	private JwtUserFactory() {
		
	}
	
	public static JwtUser create(User user) {
		return new JwtUser(String.valueOf(user.getId()), user.getEmail(), user.getPassword(), mapToGrantedAuthorities(user.getProfile()));
		
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profile) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		authorities.add(new SimpleGrantedAuthority(profile.toString()));
		
		return authorities;
	}

}
