package io.humourmind.todo;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.ServerResponse.created;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication(proxyBeanMethods = false)
@ConfigurationPropertiesScan
@CrossOrigin()
public class TodoApplication {

  public static void main(String[] args) {
    SpringApplication.run(TodoApplication.class, args);
  }

  @Bean
  RouterFunction<ServerResponse> staticResourceRouter() {
    return RouterFunctions.resources("/**", new ClassPathResource("static/"))
        .andRoute(
            GET("/"),
            req ->
                ok().contentType(MediaType.TEXT_HTML)
                    .body(new ClassPathResource("static/index.html")));
  }

  @RouterOperations({
    @RouterOperation(
        path = "/todo",
        beanClass = TodoService.class,
        beanMethod = "findAllBySort",
        method = RequestMethod.GET),
    @RouterOperation(
        path = "/todo/{id}",
        beanClass = TodoService.class,
        beanMethod = "findById",
        method = RequestMethod.GET,
        operation =
            @Operation(
                operationId = "findById",
                parameters = {
                  @Parameter(in = PATH, name = "id", description = "todo-id to find")
                })),
    @RouterOperation(
        path = "/todo",
        beanClass = TodoService.class,
        beanMethod = "save",
        method = {POST, PUT}),
    @RouterOperation(
        path = "/todo/{id}",
        beanClass = TodoService.class,
        beanMethod = "deleteById",
        method = DELETE,
        operation =
            @Operation(
                operationId = "deleteById",
                parameters = {
                  @Parameter(in = PATH, name = "id", description = "todo-id to delete")
                }))
  })
  @Bean
  RouterFunction<ServerResponse> routeHandler(
      ITodoService todoService, FindLocationConfig locationConfig) {
    return RouterFunctions.route()
        .path(
            "/todo",
            builder ->
                builder
                    .GET(
                        "/{id}",
                        req ->
                            ok().contentType(MediaType.APPLICATION_JSON)
                                .body(
                                    todoService
                                        .findById(UUID.fromString(req.pathVariable("id")))
                                        .get()))
                    .POST(
                        req ->
                            created(null)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(todoService.save(req.body(Todo.class))))
                    .PUT(
                        req ->
                            ok().contentType(MediaType.APPLICATION_JSON)
                                .body(todoService.save(req.body(Todo.class))))
                    .DELETE(
                        "/{id}",
                        req -> {
                          todoService.deleteById(UUID.fromString(req.pathVariable("id")));
                          return ok().build();
                        })
                    .GET(
                        req ->
                            ok().contentType(MediaType.APPLICATION_JSON)
                                .body(
                                    todoService.findAllBySort(Sort.by(Sort.Direction.DESC, "id")))))
        .build();
  }
}
