package pl.uniq.board.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uniq.auth.user.User;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_board_followers")
public class Follow {

	@Id
	@GeneratedValue
	private UUID follow_id;

	@ManyToOne
	@JoinColumn(name = "from_user_fk")
	private User from;

	@ManyToOne
	@JoinColumn(name = "to_board_fk")
	private Board to;
}