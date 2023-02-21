package io.humourmind.todo;

import java.sql.SQLType;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;
import org.hibernate.usertype.UserType;

@Entity
@Table(name = "todo")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Todo {

	@Id
//	@Column(updatable = false, nullable = false, columnDefinition = "uuid DEFAULT uuid_generate_v4()", name = "id")
//	@GeneratedValue(generator = "UUID4")
//	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@UuidGenerator
	private UUID id;

	private String task;

	private boolean status;

	private LocalDateTime created_at;

}
