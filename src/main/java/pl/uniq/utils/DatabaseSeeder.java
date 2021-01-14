package pl.uniq.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.uniq.auth.user.Role;
import pl.uniq.auth.user.User;
import pl.uniq.auth.user.UserRepository;
import pl.uniq.board.models.Board;
import pl.uniq.board.repository.BoardRepository;
import pl.uniq.photo.models.Photo;
import pl.uniq.photo.repository.PhotoRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@RestController
@RequestMapping(value = "/seeder")
public class DatabaseSeeder {

	final UserRepository userRepository;
	final BoardRepository boardRepository;
	final PhotoRepository photoRepository;
	final PasswordEncoder passwordEncoder;

	@Autowired
	DatabaseSeeder(UserRepository userRepository, BoardRepository boardRepository, PhotoRepository photoRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.boardRepository = boardRepository;
		this.photoRepository = photoRepository;
		this.passwordEncoder = passwordEncoder;
	}

	String seed() {
		User user1 = User.builder()
				.username("Johny Baked")
				.email("johny.backed@gmail.com")
				.password(passwordEncoder.encode("j0hnyb4k3d"))
				.isActive(true)
				.roles(Set.of(Role.UNIQ_USER))
				.build();
		User user2 = User.builder()
				.username("Joe Mama")
				.email("joe.mama@gmail.com")
				.password(passwordEncoder.encode("j03m4m4"))
				.isActive(true)
				.roles(Set.of(Role.UNIQ_USER))
				.build();
		User user3 = User.builder()
				.username("Johannes Paul")
				.email("johannes.paul@gmail.com")
				.password(passwordEncoder.encode("j0h4nn3sp4ul"))
				.isActive(false)
				.roles(Set.of(Role.UNIQ_USER))
				.build();

		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);

		Board board1 = Board.builder()
				.name("Weeding")
				.description("420 EO EO")
				.user(user1)
				.isPrivate(false)
				.timestamp(Date.from(Instant.now()))
				.build();
		Board board2 = Board.builder()
				.name("Foxes")
				.description("Foxes are dogs with mentality of cat")
				.user(user2)
				.isPrivate(true)
				.timestamp(Date.from(Instant.now()))
				.build();
		Board board3 = Board.builder()
				.name("Random")
				.description("Random photos from internet make me more thinking")
				.user(user3)
				.isPrivate(true)
				.timestamp(Date.from(Instant.now()))
				.build();

		boardRepository.save(board1);
		boardRepository.save(board2);
		boardRepository.save(board3);

		Photo photo11 = Photo.builder()
				.board(board1)
				.value("5fd27878a7a76140f9cedb60")
				.order(1)
				.build();
		Photo photo12 = Photo.builder()
				.board(board1)
				.value("5fd2794ff7ccd7d7e7b78d51")
				.order(2)
				.build();
		Photo photo13 = Photo.builder()
				.board(board1)
				.value("5fd279882a6be6c7fa914418")
				.order(3)
				.build();

		Photo photo21 = Photo.builder()
				.board(board2)
				.value("5fd27a9673c6cb3732022459")
				.order(1)
				.build();
		Photo photo22 = Photo.builder()
				.board(board2)
				.value("5fd279a6605c1ba529a3af41")
				.order(2)
				.build();
		Photo photo23 = Photo.builder()
				.board(board2)
				.value("5fd27ad33a7cb1bcba52ddc3")
				.order(3)
				.build();

		Photo photo31 = Photo.builder()
				.board(board3)
				.value("5fd27ae676dfa96110489f46")
				.order(1)
				.build();
		Photo photo32 = Photo.builder()
				.board(board3)
				.value("5fd27afddd616947e272b39c")
				.order(2)
				.build();
		Photo photo33 = Photo.builder()
				.board(board3)
				.value("5fd27b132cec9e5edeb64d44")
				.order(3)
				.build();

		List.of(photo11, photo12, photo13, photo21, photo22, photo23, photo31, photo32, photo33)
				.forEach(photoRepository::save);
		return "database filled with mock data";
	}

	@PostMapping(value = "/exec")
	public ResponseEntity<String> exec() {
		return new ResponseEntity<>(seed(), HttpStatus.CREATED);
	}
}
