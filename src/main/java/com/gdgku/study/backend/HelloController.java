package com.gdgku.study.backend;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

	private final TaskRepository repository;

	public HelloController(TaskRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/tasks")
	public List<Task> list() {
		return repository.findAll();
	}

	@GetMapping("/task")
	public ResponseEntity<Task> view(@RequestParam(name = "id") Long id) {
		return repository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/task")
	public Task add(@RequestParam(name = "content") String content) {
		return repository.save(new Task(content));
	}

	@DeleteMapping("/task")
	public ResponseEntity<Void> delete(@RequestParam(name = "id") Long id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
