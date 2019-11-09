package br.com.api.response;

public class TokenResponse {
	
	public TokenResponse() {
	}

	public TokenResponse(String token) {
		this.token = token;
	}

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
