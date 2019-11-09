package br.com.api;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.api.entity.User;
import br.com.api.enums.ProfileEnum;
import br.com.api.repository.UserRepository;

@SpringBootApplication
public class JwtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepository, passwordEncoder);
		};
	}

	private void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		User admin = new User();
		String email = "vinigomes47@bol.com.br";
		
		admin.setEmail(email);
		admin.setPassword(passwordEncoder.encode("123456"));
		admin.setProfile(ProfileEnum.ROLE_ADMIN);
		
		Optional<User> find = userRepository.findByEmailIgnoreCase(email);
		
		if ( !find.isPresent() ) {
			userRepository.save(admin);
		}
	}
}
