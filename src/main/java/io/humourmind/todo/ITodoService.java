package io.humourmind.todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;

public interface ITodoService {

  List<Todo> findAllBySort(Sort sortOrder);

  Optional<Todo> findById(UUID id);

  Todo save(Todo resource);

  void deleteById(UUID id);
}
