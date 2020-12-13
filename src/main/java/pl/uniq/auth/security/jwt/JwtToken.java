package pl.uniq.auth.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtToken {
	private final static Logger logger = LoggerFactory.getLogger(JwtToken.class);

	private final String jwtSecret = "";
	private static final long timeExpirationSeconds = 604800L;
	private static final String tokenHeader = "Authorization";
	private static final String tokenPrefix = "Bearer ";

	public String generateToken(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Date expirationDate = Date.from(Instant.now().plusSeconds(timeExpirationSeconds));

		return Jwts.builder().
				setId(UUID.randomUUID().toString()).
				setSubject(userDetails.getUsername()).
				setIssuedAt(new Date()).setExpiration(expirationDate).
				signWith(getSecret(), SignatureAlgorithm.HS512).
				compact();
	}

	private String getUsernameFromToken(String token) {
		return buildParser().
				parseClaimsJws(token).
				getBody().
				getSubject();
	}

	private String getTokenId(String token) {
		return buildParser().
				parseClaimsJws(token).
				getBody().
				getId();
	}

	private Date getTokenExpirationDate(String token) {
		return buildParser().
				parseClaimsJws(token).
				getBody().
				getExpiration();
	}

	public boolean checkJtwIsValid(String token) {
		try {
			buildParser().parseClaimsJws(token);
			return true;
		} catch (SecurityException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}


	public String parseJwt(HttpServletRequest request) {
		String header = request.getHeader(tokenHeader);
		if (header.startsWith(tokenPrefix)) {
			return header.replace(tokenPrefix, "");
		}
		return null;
	}

	public String parseJwt(String token) {
		if (token.startsWith(tokenPrefix)) {
			return token.replace(tokenPrefix, "");
		}
		return null;
	}

	private JwtParser buildParser() {
		return Jwts.parserBuilder().setSigningKey(jwtSecret).build();
	}

	private SecretKey getSecret() {
		byte[] decodedJwt = Base64.getDecoder().decode(jwtSecret);
		return Keys.hmacShaKeyFor(decodedJwt);
	}
}
