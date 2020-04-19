package com.rest.webservices.restfullwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserJPAResource {
	@Autowired
	private UserRepository repository;

	@Autowired
	private PostRepository postRepository;
	
	
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return repository.findAll();
	}

	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable Integer id) {
		Optional<User> user = repository.findById(id);
		if (!user.isPresent())
			throw new UserNotFoundException("id-" + id);
		EntityModel<User> resource = new EntityModel<User>(user.get());
		Link linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users");
		resource.add(linkTo);
		return resource;
	}

	@PostMapping("/jpa/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser;
		try {
			savedUser = repository.save(user);
		} catch (Exception e) {
			throw new UserCreationException("user-" + user);
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable Integer id) {
		repository.deleteById(id);
	}
	
	@GetMapping("jpa/users/{id}/posts")
	public List<Post> retrievePostsFromUser(@PathVariable Integer id) {
		Optional<User> user = repository.findById(id);
		if (!user.isPresent())
			throw new UserNotFoundException("id-" + id);
		return user.get().getPosts();
	}
	
	@PostMapping("jpa/users/{id}/posts") 
	public ResponseEntity<Object> createPostForUser(@PathVariable Integer id, @RequestBody Post post) {
		Optional<User> user = repository.findById(id);
		if (!user.isPresent())
			throw new UserNotFoundException("id-" + id);
		
		post.setUser(user.get());
		postRepository.save(post);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
}
