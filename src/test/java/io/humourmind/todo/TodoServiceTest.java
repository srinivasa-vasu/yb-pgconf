package io.humourmind.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = NONE)
public class TodoServiceTest {

	@Autowired
	private ITodoRepository todoRepository;

	@MockBean
	private ITodoService todoService;

	@MockBean
	private FindLocationConfig locationConfig;

	@BeforeEach
	void init() {
		todoRepository.deleteAll();
	}

	@Test
	void shouldCreateOneRecord() {
		final var todo = todoRepository.save(new Todo());
		assertThat(todoRepository.findById(todo.getId()).get()).isEqualTo(todo);
	}

}
