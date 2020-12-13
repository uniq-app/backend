package pl.uniq.auth.security.jwt;

import org.springframework.web.filter.OncePerRequestFilter;
import pl.uniq.auth.security.userdetails.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

	private  CustomUserDetailsService customUserDetailsService;
	private JwtTokenService jwtTokenService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

	}
}
