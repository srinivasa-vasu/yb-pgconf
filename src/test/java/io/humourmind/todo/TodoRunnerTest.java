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
@ContextConfiguration(classes = { TodoRunnerTest.Config.class })
@AutoConfigureTestDatabase(replace = NONE)
// @Testcontainers
// @ActiveProfiles("psqltest")
@ActiveProfiles("ysqltest")
@Rollback(false)
public class TodoRunnerTest {

	@Configuration
	@EnableTransactionManagement
	@EnableJpaRepositories
	@EntityScan
	static class Config {

	}

	@Autowired
	private EntityManager entityManager;

	@Test
	void singleIdentityFlow() {
		entityManager.persist(new TodoIdentity());
	}

	@Test
	void groupIdentityFlow() {
		for (int i = 0; i < 10; i++) {
			entityManager.persist(new TodoIdentity());
		}
	}

	@Test
	void partitionIdentityFlow() {
		entityManager.persist(new TodoCompositeIdentity());
	}

	@Test
	void singleSequenceFlow() {
		entityManager.persist(new TodoSequence());
	}

	@Test
	void nativeSequenceFlow() {
		String query = "explain analyze insert into todo_s(id, task, status) values(nextval('todo_sc_id_seq'), 'test', false)";
		nativeExecution(query, 105);
	}

	@Test
	void groupSequenceFlow() {
		for (int i = 0; i < 10; i++) {
			entityManager.persist(new TodoSequence());
		}
	}

	@Test
	void partitionSequenceFlow() {
		entityManager.persist(new TodoCompositeSequence());
	}

	@Test
	void partitionGroupSequenceFlow() {
		for (int i = 0; i < 10; i++) {
			entityManager.persist(new TodoCompositeSequence());
		}
	}

	@Test
	void singleUUIDFlow() {
		entityManager.persist(new Todo());
	}

	@Test
	void groupUUIDFlow() {
		for (int i = 0; i < 10; i++) {
			entityManager.persist(new Todo());
		}
	}

	@Test
	void nativeUUIDFlow() {
		String query = "explain analyze insert into todo(task, status) values('test', false)";
		nativeExecution(query, 50);
	}

	@SuppressWarnings("unchecked")
	void nativeExecution(String query, int count) {
		for (int i = 1; i <= count; i++) {
			System.out.println("---------------------------------------\n");
			entityManager.createNativeQuery(query).getResultStream().forEach(System.out::println);
			System.out.println("Iteration Id:" + i);
			System.out.println("---------------------------------------\n");
		}

	}

}
