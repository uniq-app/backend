package pl.uniq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.uniq.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

	private User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles().stream()
				.map(role ->  new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public String getEmail() { return user.getEmail(); }

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isActive();
	}
}
