package pl.uniq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.model.JwtTokenEntity;
import pl.uniq.repository.JwtTokenRepository;

import java.util.UUID;

@Service
public class JwtTokenService {

	private final JwtTokenRepository jwtTokenRepository;

	@Autowired
	public JwtTokenService(JwtTokenRepository jwtTokenRepository) {
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
