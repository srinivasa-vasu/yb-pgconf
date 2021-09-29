package io.humourmind.todo;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import javax.sql.DataSource;

import com.yugabyte.ysql.YBClusterAwareDataSource;
import com.zaxxer.hikari.HikariDataSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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

	//@Bean
	public DataSource ybDataSource(DataSourceProperties properties) {
		YBClusterAwareDataSource wrappedDataSource = (YBClusterAwareDataSource) properties.initializeDataSourceBuilder()
				.type(YBClusterAwareDataSource.class).build();
		wrappedDataSource.setLoadBalanceHosts(true);
		// wrappedDataSource.setReWriteBatchedInserts(true);
		wrappedDataSource.setLoadBalance("true");
		wrappedDataSource.setAdditionalEndpoints("127.0.0.4:5433");
		HikariDataSource hikariDataSource = new HikariDataSource();
		hikariDataSource.setDataSource(wrappedDataSource);
		hikariDataSource.setConnectionTestQuery("SELECT 1");
		// hikariDataSource.setMaximumPoolSize(120);
		return hikariDataSource;
	}

	@Bean
	RouterFunction<ServerResponse> staticResourceRouter() {
		return RouterFunctions.resources("/**", new ClassPathResource("static/")).andRoute(GET("/"),
				req -> ok().contentType(MediaType.TEXT_HTML).body(new ClassPathResource("static/index.html")));
	}

	@RouterOperations({
			@RouterOperation(path = "/v1/todo", beanClass = TodoService.class, beanMethod = "findAllBySort",
					method = RequestMethod.GET),
			@RouterOperation(path = "/v1/todo/{id}", beanClass = TodoService.class, beanMethod = "findById",
					method = RequestMethod.GET,
					operation = @Operation(operationId = "findById",
							parameters = { @Parameter(in = PATH, name = "id", description = "todo-id to find") })),
			@RouterOperation(path = "/v1/todo", beanClass = TodoService.class, beanMethod = "save",
					method = { POST, PUT }),
			@RouterOperation(path = "/v1/todo/{id}", beanClass = TodoService.class, beanMethod = "deleteById",
					method = DELETE,
					operation = @Operation(operationId = "deleteById",
							parameters = { @Parameter(in = PATH, name = "id", description = "todo-id to delete") })) })
	@Bean
	RouterFunction<ServerResponse> routeHandler(ITodoService todoService, FindLocationConfig locationConfig) {
		return RouterFunctions.route()
				.path("/v1/todo", builder -> builder
						.GET("/{id}",
								req -> ok().contentType(MediaType.APPLICATION_JSON)
										.body(Objects.requireNonNull(todoService
												.findById(UUID.fromString(req.pathVariable("id"))).orElse(null))))
						.POST(req -> {
							Todo todo = req.body(Todo.class);
							todo.setGeoCode(locationConfig.getGeoCode());
							return created(new URI("/v1/todo")).contentType(MediaType.APPLICATION_JSON)
									.body(todoService.save(todo));
						}).PUT(req -> ok().contentType(MediaType.APPLICATION_JSON)
								.body(todoService.save(req.body(Todo.class))))
						.DELETE("/{id}", req -> {
							todoService.deleteById(UUID.fromString(req.pathVariable("id")));
							return ok().build();
						})
						.GET("/page/{limit}",
								req -> ok().contentType(MediaType.APPLICATION_JSON)
										.body(todoService.findByLimit(Integer.parseInt(req.pathVariable("limit")))))
						.GET(req -> ok().contentType(MediaType.APPLICATION_JSON)
								.body(todoService.findAllBySort(Sort.by(Sort.Direction.DESC, "id")))))
				.build();
	}

}
