package br.com.api.service;

import br.com.api.entity.User;

public interface UserService {
	
	User findByEmail(String email);
}
