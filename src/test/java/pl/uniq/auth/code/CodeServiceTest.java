package pl.uniq.auth.code;

import org.junit.jupiter.api.Test;
import pl.uniq.auth.user.User;
import pl.uniq.board.models.Board;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class CodeServiceTest {

	@Test
	void test1() {
		CodeRepository codeRepository = mock(CodeRepository.class);
		when(codeRepository.save(any(Code.class))).thenAnswer(i -> i.getArguments()[0]);
		CodeService codeService = new CodeService(codeRepository);
		Code code = codeService.generateCode(UUID.randomUUID());

		verify(codeRepository, times(1)).save(any(Code.class));
		assertEquals(6, String.valueOf(code.getValue())
				.length());
		assertTrue(code.getValue() > 100000);
		assertTrue(code.getValue() < 999999);
	}

	@Test
	void test2() {
		Code code = Code.builder()
				.value(213769)
				.build();
		CodeRepository codeRepository = mock(CodeRepository.class);
		doReturn(code).when(codeRepository)
				.getCodeByValue(213769);
		CodeService codeService = new CodeService(codeRepository);
		Code result = codeService.getCodeByValue(213769);
		assertEquals(code, result);
	}

	@Test
	void test3() {
		UUID userId = UUID.randomUUID();
		User user = User.builder()
				.userId(userId)
				.build();
		Code code = Code.builder()
				.value(213769)
				.userId(userId)
				.build();
		CodeRepository codeRepository = mock(CodeRepository.class);
		doReturn(code).when(codeRepository)
				.getCodeByUserId(userId);
		CodeService codeService = new CodeService(codeRepository);
		Code result = codeService.getCodeByUser(user);
		assertEquals(code, result);
	}

	@Test
	void test4() {
		Code code1 = Code.builder()
				.expiresAt(Date.from(Instant.now()
						.plusSeconds(2137L)))
				.build();
		Code code2 = Code.builder()
				.expiresAt(Date.from(Instant.now()
						.minusSeconds(2137L)))
				.build();
		CodeRepository codeRepository = mock(CodeRepository.class);
		CodeService codeService = new CodeService(codeRepository);
		boolean result1 = codeService.validToken(code1);
		boolean result2 = codeService.validToken(code2);
		assertTrue(result1);
		assertFalse(result2);
	}

	@Test
	void test5()
	{
		Code code = Code.builder()
				.expiresAt(Date.from(Instant.now()
						.minusSeconds(2137L)))
				.build();
		CodeRepository codeRepository = mock(CodeRepository.class);
		CodeService codeService = new CodeService(codeRepository);
		codeService.deleteCode(code);
		verify(codeRepository, times(1)).delete(code);
	}
}
