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

	public void addToken(String token)
	{
		UUID tokenUuid = UUID.nameUUIDFromBytes(token.getBytes());
		jwtTokenRepository.save(JwtTokenEntity.builder().
				token_id(tokenUuid).
				token(token).
				isActive(true).build());
	}

	public void revokeToken(String token) {
		UUID tokenUuid = UUID.nameUUIDFromBytes(token.getBytes());
		JwtTokenEntity jwt = jwtTokenRepository.findById(tokenUuid).get();
		jwt.setIsActive(false);
		jwtTokenRepository.save(jwt);
	}

	public boolean isTokenRevoked(UUID uuid) {
		return jwtTokenRepository.existsById(uuid);
	}
}
