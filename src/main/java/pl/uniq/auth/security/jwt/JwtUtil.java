package pl.uniq.auth.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtUtil {
	private final static String TOKEN_PREFIX = "Bearer ";
	private final static String SECRET = "jlvF4FR4lfWW5LF8BBmFcEWGtggbAqdpsCa9co8cQpp5wzzKmJwQIGjH5XwaxP2CGSBxlbnqpTUhI7U49Tjs5zOTkW1Pk7WHC";
	private final static Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
	private static final Long timeExpirationSeconds = 604800L;

	public String parseHeader(String header) {
		if (header.startsWith(TOKEN_PREFIX)) {
			return header.replace(TOKEN_PREFIX, "");
		} else {
			return null;
		}
	}

	public Date extractExpirationDate(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().
				setClaims(claims).
				setSubject(subject).
				setIssuedAt(new Date(System.currentTimeMillis())).
				setExpiration(Date.from(Instant.now().plusSeconds(timeExpirationSeconds))).
				signWith(KEY).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private Boolean isTokenExpired(String token) {
		return extractExpirationDate(token).before(new Date());
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(KEY).build();
		return jwtParser.parseClaimsJws(token).getBody();
	}
}
