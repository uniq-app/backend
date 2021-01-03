package pl.uniq.auth.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JwtTokenService {

	private final JwtTokenRepository jwtTokenRepository;

	@Autowired
	public JwtTokenService(JwtTokenRepository jwtTokenRepository, JwtUtil jwtUtil) {
		this.jwtTokenRepository = jwtTokenRepository;
	}

	public void addToken(String token) {
		UUID tokenUuid = UUID.nameUUIDFromBytes(token.getBytes());
		jwtTokenRepository.save(JwtTokenEntity.builder().
				tokenId(tokenUuid).
				token(token).
				isActive(true).build());
	}

	public void deleteToken(String token) {
		UUID tokenUuid = UUID.nameUUIDFromBytes(token.getBytes());
		jwtTokenRepository.deleteByTokenId(tokenUuid);
	}
}
