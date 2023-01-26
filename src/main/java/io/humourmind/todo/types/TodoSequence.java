package io.humourmind.todo.types;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo_s")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoSequence {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_s_id_seq")
	@SequenceGenerator(name = "todo_s_id_seq", sequenceName = "todo_s_id_seq", allocationSize = 1)
	private Long id;

	private String task;

	private LocalDateTime created_at = LocalDateTime.now();

	private boolean status;

}
