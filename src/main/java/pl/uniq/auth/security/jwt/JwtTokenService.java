package pl.uniq.auth.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JwtTokenService {

	private final JwtTokenRepository jwtTokenRepository;

	@Autowired
	public JwtTokenService(JwtTokenRepository jwtTokenRepository) {
		this.jwtTokenRepository = jwtTokenRepository;
	}

	public void revokeToken(String token) {
		jwtTokenRepository.save(new JwtTokenEntity(UUID.nameUUIDFromBytes(token.getBytes())));
	}

	public boolean isTokenRevoked(UUID uuid) {
		return jwtTokenRepository.existsById(uuid);
	}
}
