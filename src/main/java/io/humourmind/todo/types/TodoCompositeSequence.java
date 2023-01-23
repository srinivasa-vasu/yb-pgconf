package io.humourmind.todo.types;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo_sp")
@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(TodoCompositeId.class)
public class TodoCompositeSequence {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_sp_id_seq")
	@SequenceGenerator(name = "todo_sp_id_seq", sequenceName = "todo_sp_id_seq", allocationSize = 1)
	private Long id;

	@Id
	private LocalDateTime created_at = LocalDateTime.now();

	private String task;

	private boolean status;

}
