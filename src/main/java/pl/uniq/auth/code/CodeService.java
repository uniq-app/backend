package pl.uniq.auth.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.uniq.user.User;
import pl.uniq.exceptions.CodeException;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class CodeService {

	private final CodeRepository codeRepository;
	private static final int MIN = 100000;
	private static final int MAX = 999999;
	private static final long DAY_IN_SECONDS = 86400L;


	@Autowired
	public CodeService(CodeRepository codeRepository) {this.codeRepository = codeRepository;}

	public Code generateCode(UUID userId) {
		Random random = new SecureRandom();
		int range = MAX - MIN + 1;
		int value = random.nextInt(range) + MIN;
		Code code = Code.builder().
				userId(userId).
				value(value).
				expiresAt(Date.from(Instant.now().plusSeconds(DAY_IN_SECONDS))).build();
		codeRepository.save(code);
		return code;
	}

	public Code getCodeByValue(int value) {
		Code code = codeRepository.getCodeByValue(value);
		if (code != null) {
			return code;
		} else {
			throw new CodeException("Wrong code");
		}
	}

	public Code getCodeByUser(User user) {
		return codeRepository.getCodeByUserId(user.getUserId());
	}

	public boolean validToken(Code code) {
		if (code.getExpiresAt().after(Date.from(Instant.now()))) {
			return true;
		} else {
			codeRepository.delete(code);
			return false;
		}
	}

	public void deleteCode(Code code) {
		codeRepository.delete(code);
	}

	public void clear(UUID userId) {
		codeRepository.deleteAllByUserId(userId);
	}
}
