package io.humourmind.todo;

import io.humourmind.todo.types.TodoCompositeIdentity;
import io.humourmind.todo.types.TodoCompositeSequence;
import io.humourmind.todo.types.TodoIdentity;
import io.humourmind.todo.types.TodoSequence;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ContextConfiguration(classes = { TodoTestRunner.Config.class })
@AutoConfigureTestDatabase(replace = NONE)
// @Testcontainers
@ActiveProfiles("test")
public class TodoTestRunner {

	@Configuration
	@EnableTransactionManagement
	@EnableJpaRepositories
	@EntityScan
	static class Config {

	}

	@Autowired
	private EntityManager entityManager;

	@Test
	@Rollback(false)
	void singleIdentityFlow() {
		entityManager.persist(new TodoIdentity());
	}

	@Test
	@Rollback(false)
	void groupIdentityFlow() {
		for (int i = 0; i < 10; i++) {
			entityManager.persist(new TodoIdentity());
		}
	}

	@Test
	@Rollback(false)
	void partitionIdentityFlow() {
		entityManager.persist(new TodoCompositeIdentity());
	}

	@Test
	@Rollback(false)
	void singleSequenceFlow() {
		entityManager.persist(new TodoSequence());
	}

	@Test
	@Rollback(false)
	void groupSequenceFlow() {
		for (int i = 0; i < 10; i++) {
			entityManager.persist(new TodoSequence());
		}
	}

	@Test
	@Rollback(false)
	void partitionSequenceFlow() {
		entityManager.persist(new TodoCompositeSequence());
	}

	@Test
	@Rollback(false)
	void partitionGroupSequenceFlow() {
		for (int i = 0; i < 10; i++) {
			entityManager.persist(new TodoCompositeSequence());
		}
	}

}
