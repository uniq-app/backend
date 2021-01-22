package pl.uniq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uniq.model.Code;

import java.util.UUID;

@Repository
public interface CodeRepository extends JpaRepository<Code, UUID> {
	Code getCodeByValue(int value);
	Code getCodeByUserId(UUID uuid);
	void deleteAllByUserId(UUID uuid);
}
