package pl.uniq.auth.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserEntityRepository extends JpaRepository<User, UUID> {
	User findByEmail(String email);
	boolean existsByEmail(String email);
}
