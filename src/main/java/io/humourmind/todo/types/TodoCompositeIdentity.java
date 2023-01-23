package io.humourmind.todo.types;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo_ip")
@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(TodoCompositeId.class)
public class TodoCompositeIdentity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Id
	private LocalDateTime created_at = LocalDateTime.now();

	private String task;


	private boolean status;

}
