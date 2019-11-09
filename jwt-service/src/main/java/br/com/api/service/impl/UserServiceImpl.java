package br.com.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.entity.User;
import br.com.api.repository.UserRepository;
import br.com.api.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User findByEmail(String email) {
		Optional<User> userOpt = this.userRepository.findByEmailIgnoreCase(email);
		
		if ( userOpt.isPresent() ) {
			return userOpt.get();
		}
		
		return null;
	}
}
