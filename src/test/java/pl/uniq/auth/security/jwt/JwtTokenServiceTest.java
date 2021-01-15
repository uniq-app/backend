package pl.uniq.auth.security.jwt;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JwtTokenServiceTest {

	@Test
	void test1()
	{
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2hueSIsImlhdCI6MTYxMDc0ODY2MywiZXhwIjoxNjExMzUzNDYzfQ.oPC0eiTNqJeIur_0lfqFmRyKujElhT40j6o_iP61j5fEE91PECeSahmv423KUs2Nch2hPypy2EfmjUIdq10fLw";
		JwtTokenRepository jwtTokenRepository = mock(JwtTokenRepository.class);
		JwtTokenService jwtTokenService = new JwtTokenService(jwtTokenRepository);
		jwtTokenService.addToken(token);
		verify(jwtTokenRepository, times(1)).save(any(JwtTokenEntity.class));
	}

	@Test
	void test2()
	{
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2hueSIsImlhdCI6MTYxMDc0ODY2MywiZXhwIjoxNjExMzUzNDYzfQ.oPC0eiTNqJeIur_0lfqFmRyKujElhT40j6o_iP61j5fEE91PECeSahmv423KUs2Nch2hPypy2EfmjUIdq10fLw";
		JwtTokenRepository jwtTokenRepository = mock(JwtTokenRepository.class);
		JwtTokenService jwtTokenService = new JwtTokenService(jwtTokenRepository);
		jwtTokenService.deleteToken(token);
		verify(jwtTokenRepository, times(1)).deleteByTokenId(any(UUID.class));
	}
}
