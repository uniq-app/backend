package pl.uniq.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.uniq.auth.security.jwt.JwtAuthEntryPoint;
import pl.uniq.auth.security.jwt.JwtTokenService;
import pl.uniq.auth.security.userdetails.CustomUserDetails;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private CustomUserDetails customUserDetails;
	private JwtTokenService jwtTokenService;
	private JwtAuthEntryPoint jwtAuthEntryPoint;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.cors().and().csrf().disable()
				.exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/auth/register").permitAll()
				.antMatchers(HttpMethod.POST, "/auth/login").permitAll()
				.antMatchers(HttpMethod.POST, "/auth/logout").permitAll()
				.antMatchers("/board/**").hasAuthority("UNIQ_USER")
				.antMatchers("/**").denyAll()
				.anyRequest().authenticated();
	}
}
