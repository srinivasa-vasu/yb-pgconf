package io.humourmind.todo;

import java.util.UUID;

import com.yugabyte.data.jdbc.repository.YsqlRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ITodoRepository extends YsqlRepository<Todo, UUID> {

}
