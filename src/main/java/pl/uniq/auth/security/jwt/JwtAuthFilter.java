package pl.uniq.auth.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.uniq.auth.security.userdetails.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

	private final static Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
	private final CustomUserDetailsService customUserDetailsService;

	public JwtAuthFilter(CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			JwtUtil jwtUtil = new JwtUtil();
			String authorizationHeader = request.getHeader("Authorization");
			String token = jwtUtil.parseHeader(authorizationHeader);
			String username = jwtUtil.extractUsername(token);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		filterChain.doFilter(request, response);
	}
}
