package com.rest.webservices.restfullwebservices.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	private static List<User> users = new ArrayList<>();
	private static int usersCont = 3;
	static {
		users.add(new User(1, "Adam", new Date(), Arrays.asList(new Post(1, Arrays.asList("detail 01", "detail 02")))));
		users.add(new User(2, "Eve", new Date()));
		users.add(new User(3, "Jach", new Date()));
	}

	public List<User> findAll() {
		return users;
	}

//	public List<Post> findAllPostFromUser(int userId) {
//		User userFound = findUser(userId);
//		return userFound == null ? new ArrayList<Post>() : userFound.getPosts();
//	}
//
//	public Post savePostToUser(int userId, Post post) {
//		User userFound = findUser(userId);
//
//		if (post.getId() == null) {
//			int postsNumber = userFound.getPosts().size();
//			post.setId(++postsNumber);
//		}
//		userFound.getPosts().add(post);
//		return post;
//	}

	public User save(User user) throws Exception {
		if (user.getId() == null)
			user.setId(++usersCont);
		users.add(user);

		return user;
	}

	public User findUser(int id) {
		Optional<User> userFound = users.stream().filter(user -> user.getId().equals(id)).findAny();
		return userFound.orElse(null);
	}

//	public List<String> findAllDetailsFromPost(Integer userId, Integer postId) {
//		Optional<Post> findFirst = users.stream().filter(i -> i.getId().equals(userId))
//				.flatMap(i -> i.getPosts().stream()).filter(i -> i.getId().equals(postId)).findFirst();
//		return findFirst.get().getDetails();
//	}

	public User deleteById(int id) {
		Optional<User> userDeleted = users.stream().filter(i -> i.getId().equals(id)).findFirst().map(i -> {
			users.remove(i);
			return i;
		});
		return userDeleted.orElse(null);
	}
}
