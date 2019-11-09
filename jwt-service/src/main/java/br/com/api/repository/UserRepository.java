package br.com.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmailIgnoreCase(String email);
}
