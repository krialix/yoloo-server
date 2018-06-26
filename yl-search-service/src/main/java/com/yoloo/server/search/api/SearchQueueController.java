package com.yoloo.server.search.api;

import com.yoloo.server.common.queue.api.YolooEvent;
import com.yoloo.server.common.queue.config.QueueEndpoint;
import com.yoloo.server.search.buddyrequest.BuddyRequestPost;
import com.yoloo.server.search.buddyrequest.BuddyRequestRepository;
import com.yoloo.server.search.post.Post;
import com.yoloo.server.search.post.PostRepository;
import com.yoloo.server.search.user.User;
import com.yoloo.server.search.user.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequestMapping(QueueEndpoint.QUEUE_SEARCH_ENDPOINT)
@RestController
public class SearchQueueController {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final BuddyRequestRepository buddyRequestRepository;

  public SearchQueueController(
      PostRepository postRepository,
      UserRepository userRepository,
      BuddyRequestRepository buddyRequestRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.buddyRequestRepository = buddyRequestRepository;
  }

  @PostMapping
  public void processSearchQueue(@RequestBody YolooEvent event) {
    switch (event.getMetadata().getType()) {
      case NEW_POST:
        createPost(event.getPayload());
        break;
      case UPDATE_POST:
        updatePost(event.getPayload());
        break;
      case DELETE_POST:
        deletePost(event.getPayload());
        break;
      case NEW_BUDDY_REQUEST:
        createBuddyRequest(event.getPayload());
        break;
      case UPDATE_BUDDY_REQUEST:
        updateBuddyRequest(event.getPayload());
        break;
      case DELETE_BUDDY_REQUEST:
        deleteBuddyRequest(event.getPayload());
        break;
      case NEW_USER:
        createUser(event.getPayload());
        break;
      case UPDATE_USER:
        updateUser(event.getPayload());
        break;
      case DELETE_USER:
        deleteUser(event.getPayload());
        break;
    }
  }

  private void deletePost(Map<String, Object> payload) {
    postRepository.deleteById((String) payload.get("id"));
  }

  private void createPost(Map<String, Object> payload) {
    //noinspection unchecked
    Post post =
        Post.newBuilder()
            .id((String) payload.get("id"))
            .title((String) payload.get("title"))
            .content((String) payload.get("content"))
            .tags((List<String>) payload.get("tags"))
            .build();

    postRepository.save(post);
  }

  private void updatePost(Map<String, Object> payload) {
    //noinspection unchecked
    postRepository
        .findById((String) payload.get("id"))
        .map(
            post ->
                post.toBuilder()
                    .content((String) payload.get("content"))
                    .tags((List<String>) payload.get("tags"))
                    .build())
        .ifPresent(postRepository::save);
  }

  private void createUser(Map<String, Object> payload) {
    User user =
        User.newBuilder()
            .id((String) payload.get("id"))
            .displayName((String) payload.get("displayName"))
            .build();

    userRepository.save(user);
  }

  private void updateUser(Map<String, Object> payload) {
    userRepository
        .findById((String) payload.get("id"))
        .map(user -> user.toBuilder().displayName((String) payload.get("displayName")).build())
        .ifPresent(userRepository::save);
  }

  private void deleteUser(Map<String, Object> payload) {
    userRepository.deleteById((String) payload.get("id"));
  }

  private void createBuddyRequest(Map<String, Object> payload) {
    BuddyRequestPost buddyRequestPost =
        BuddyRequestPost.newBuilder()
            .id((String) payload.get("id"))
            .content((String) payload.get("content"))
            .fromPeople((Integer) payload.get("fromPeople"))
            .toPeople((Integer) payload.get("toPeople"))
            .fromDate(LocalDate.parse((CharSequence) payload.get("fromDate")))
            .toDate(LocalDate.parse((CharSequence) payload.get("toDate")))
            .build();

    buddyRequestRepository.save(buddyRequestPost);
  }

  private void updateBuddyRequest(Map<String, Object> payload) {
    buddyRequestRepository
        .findById((String) payload.get("id"))
        .map(
            buddyRequestPost ->
                buddyRequestPost
                    .toBuilder()
                    .content((String) payload.get("content"))
                    .fromPeople((Integer) payload.get("fromPeople"))
                    .toPeople((Integer) payload.get("toPeople"))
                    .fromDate(LocalDate.parse((CharSequence) payload.get("fromDate")))
                    .toDate(LocalDate.parse((CharSequence) payload.get("toDate")))
                    .build())
        .ifPresent(buddyRequestRepository::save);
  }

  private void deleteBuddyRequest(Map<String, Object> payload) {
    buddyRequestRepository.deleteById((String) payload.get("id"));
  }
}
