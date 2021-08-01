package io.humourmind.todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TodoService implements ITodoService {

	private final ITodoRepository todoRepository;

	TodoService(ITodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@Override
	public List<Todo> findAllBySort(Sort sortOrder) {
		return todoRepository.findAll(sortOrder);
	}

	@Override
	public Page<Todo> findByLimit(int limit) {
		return todoRepository.findAll(PageRequest.ofSize(limit));
	}

	@Override
	public Optional<Todo> findById(UUID id) {
		return todoRepository.findById(id);
	}

	@Override
	public Todo save(Todo resource) {
		return todoRepository.save(resource);
	}

	@Override
	public void deleteById(UUID id) {
		todoRepository.deleteById(id);
	}

}
