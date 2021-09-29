package io.humourmind.todo.tips;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
	@SequenceGenerator(name = "todo_s_id_seq", sequenceName = "todo_s_id_seq")
	private Long id;

	private String task;

	private String geoCode;

	private boolean status;

}
