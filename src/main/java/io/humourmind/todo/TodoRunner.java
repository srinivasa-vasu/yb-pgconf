package io.humourmind.todo;

import javax.persistence.EntityManager;

import io.humourmind.todo.tips.TodoIdentity;
import io.humourmind.todo.tips.TodoSequence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class TodoRunner implements CommandLineRunner {

	private final EntityManager entityManager;

	private final PlatformTransactionManager transactionManager;

	TodoRunner(EntityManager entityManager, PlatformTransactionManager transactionManager) {
		this.entityManager = entityManager;
		this.transactionManager = transactionManager;
	}

	@Override
	public void run(String... args) {
		identityTipFlow();
		sequenceTipFlow();
	}

	void identityTipFlow() {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			for (int i = 0; i < 10; i++) {
				entityManager.persist(new TodoIdentity());
			}
			transactionManager.commit(status);
		}
		catch (DataAccessException exception) {
			transactionManager.rollback(status);
		}
	}

	void sequenceTipFlow() {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			for (int i = 0; i < 10; i++) {
				entityManager.persist(new TodoSequence());
			}
			transactionManager.commit(status);
		}
		catch (DataAccessException exception) {
			transactionManager.rollback(status);
		}
	}

}
