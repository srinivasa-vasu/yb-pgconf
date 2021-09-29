package io.humourmind.todo;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.YugabyteYSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TodoRepositoryTest {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ITodoRepository todoRepository;

	@ClassRule
	public static YugabyteYSQLContainer container = new YugabyteYSQLContainer("yugabytedb/yugabyte:2.7.2.0-b216")
			.withDatabaseName("yugabyte").withUsername("yugabyte").withPassword("yugabyte");

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues
					.of("spring.datasource.url=" + container.getJdbcUrl(),
							"spring.datasource.username=" + container.getUsername(),
							"spring.datasource.password=" + container.getPassword())
					.applyTo(configurableApplicationContext.getEnvironment());
		}

	}

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(todoRepository).isNotNull();
	}

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
