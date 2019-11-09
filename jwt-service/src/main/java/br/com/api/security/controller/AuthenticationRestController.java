package br.com.api.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.response.TokenResponse;
import br.com.api.security.request.JwtAuthenticationRequest;
import br.com.api.security.service.JwtUserDetailServiceImpl;
import br.com.api.security.utils.JwtTokenUtil;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailServiceImpl userDetailsService;
	
	@PostMapping(value = "/api/token/auth")
	public ResponseEntity<TokenResponse> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new TokenResponse(token));
	}
	
	@PostMapping(value = "/api/token/refresh")
	public ResponseEntity<TokenResponse> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		
		if ( !token.isEmpty() && jwtTokenUtil.canTokenBeRefreshed(token)  ) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new TokenResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
}
