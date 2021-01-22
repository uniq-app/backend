package pl.uniq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.model.JwtTokenEntity;

import java.util.UUID;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtTokenEntity, UUID> {
	JwtTokenEntity findByTokenId(UUID uuid);
	void deleteByTokenId(UUID uuid);
}
