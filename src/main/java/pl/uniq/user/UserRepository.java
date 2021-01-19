package pl.uniq.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

	@Modifying
	@Query(value = "UPDATE uniq_user SET fcm_token = null WHERE username = ?1", nativeQuery = true)
	void removeFCMTokenByUsername(String username);

	@Modifying
	@Query(value = "UPDATE uniq_user SET fcm_token = null WHERE user_id = ?1", nativeQuery = true)
	void removeFCMTokenByUserId(UUID userId);
}
