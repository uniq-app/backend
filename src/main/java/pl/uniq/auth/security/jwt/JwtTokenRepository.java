package pl.uniq.auth.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtTokenEntity, UUID> {}
