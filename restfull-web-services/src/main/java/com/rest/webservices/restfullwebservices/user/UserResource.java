package com.rest.webservices.restfullwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

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
public class UserResource {
	@Autowired
	private UserDaoService service;

	// Hypermedia as the Engine of Application State (HATEOAS)
	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}

	@GetMapping("users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable Integer id) {
		User userFound = service.findUser(id);
		if (userFound == null)
			throw new UserNotFoundException("id-" + id);
		EntityModel<User> resource = new EntityModel<User>(userFound);
		Link linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users");
		resource.add(linkTo);
		return resource;
	}

//	@GetMapping("users/{id}/posts")
//	public List<Post> retrievePostsFromUser(@PathVariable Integer id) {
//		return service.findAllPostFromUser(id);
//	}

//	@GetMapping("users/{userId}/posts/{postId}")
//	public List<String> retrievePostsDetailsFromPost(@PathVariable Integer userId, @PathVariable Integer postId) {
//		return service.findAllDetailsFromPost(userId, postId);
//	}

//	@PostMapping("users/{id}/posts")
//	public ResponseEntity<Object> createPostForUser(@PathVariable Integer id, @RequestBody Post post) {
//		Post savedPost = service.savePostToUser(id, post);
//
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
//				.toUri();
//		return ResponseEntity.created(location).build();
//	}

	@PostMapping("users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser;
		try {
			savedUser = service.save(user);
		} catch (Exception e) {
			throw new UserCreationException("user-" + user);
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("users/{id}")
	public void deleteUser(@PathVariable Integer id) {
		User deletedUser = service.deleteById(id);
		if (deletedUser == null)
			throw new UserNotFoundException("id-" + id);
	}
}
