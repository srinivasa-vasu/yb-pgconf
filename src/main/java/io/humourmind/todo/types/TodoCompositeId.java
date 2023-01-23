package io.humourmind.todo.types;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TodoCompositeId implements Serializable {

	private Long id;

	private LocalDateTime created_at;

}
