package pl.uniq.auth.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findUserByUserId(UUID uuid);
	User findByUsername(String username);
	Optional<User> findUserByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
