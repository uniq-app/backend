package pl.uniq.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.uniq.auth.security.jwt.JwtAuthEntryPoint;
import pl.uniq.auth.security.jwt.JwtAuthFilter;
import pl.uniq.auth.security.userdetails.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomUserDetailsService customUserDetailsService;
	private final JwtAuthEntryPoint jwtAuthEntryPoint;
	private final JwtAuthFilter jwtAuthFilter;

	@Autowired
	public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthEntryPoint jwtAuthEntryPoint) {
		this.customUserDetailsService = customUserDetailsService;
		this.jwtAuthEntryPoint = jwtAuthEntryPoint;
		this.jwtAuthFilter = new JwtAuthFilter(customUserDetailsService);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
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
				.antMatchers(HttpMethod.POST, "/auth/logout").hasAnyAuthority("UNIQ_USER")
				.antMatchers(HttpMethod.POST, "/user/forgot").permitAll()
				.antMatchers(HttpMethod.POST, "/user/valid").permitAll()
				.antMatchers(HttpMethod.POST, "/user/resend").permitAll()
				.antMatchers(HttpMethod.PUT, "/user/activation").permitAll()
				.antMatchers(HttpMethod.PUT, "/user/reset").permitAll()
				.antMatchers("/profile/**").hasAuthority("UNIQ_USER")
				.antMatchers("/boards/**").hasAuthority("UNIQ_USER")
				.antMatchers("/user/**").hasAuthority("UNIQ_USER")
				.antMatchers("/**").denyAll()
				.anyRequest().authenticated();
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs",
				"/v3/api-docs",
				"/configuration/**",
				"/swagger-resources/**",
				"/swagger-ui/**",
				"/swagger-ui/",
				"/swagger-ui.html",
				"/webjars/**",
				"/api-docs/**");
	}
}
