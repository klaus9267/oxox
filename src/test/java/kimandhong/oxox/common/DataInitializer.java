package kimandhong.oxox.common;

import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.domain.Vote;
import kimandhong.oxox.dto.post.CreatePostDto;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.repository.PostRepository;
import kimandhong.oxox.repository.UserRepository;
import kimandhong.oxox.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@TestConfiguration
public class DataInitializer {
  @Autowired
  UserRepository userRepository;
  @Autowired
  VoteRepository voteRepository;
  @Autowired
  PostRepository postRepository;
//  @Autowired
//  PasswordEncoder passwordEncoder;

  private List<User> savedUsers = new ArrayList<>();
  private List<Post> savedPosts = new ArrayList<>();
  private List<Vote> savedVotes = new ArrayList<>();

  public void init() {
    this.initUsers();
    this.initPosts();
    this.initVotes();
  }

  private void initUsers() {
    List<User> users = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      JoinDto joinDto = new JoinDto("email" + random.nextInt(9999), null, "nickname" + random.nextInt(9999));
      User user = User.from(joinDto, "password", 1L, null);
      users.add(user);
    }
    savedUsers = userRepository.saveAll(users);
  }

  private void initPosts() {
    List<Post> posts = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      int randomUserId = random.nextInt(savedUsers.size());
      CreatePostDto createPostDto = new CreatePostDto("title" + random.nextInt(9999), "content" + random.nextInt(9999));
      Post post = Post.from(createPostDto, savedUsers.get(randomUserId), null);
      posts.add(post);
    }
    savedPosts = postRepository.saveAll(posts);
  }

  private void initVotes() {
    List<Vote> votes = new ArrayList<>();
    Random random = new Random();

    A:
    for (int i = 0; i < 1000; i++) {
      int randomUserId = random.nextInt(savedUsers.size());
      Post post = savedPosts.get(random.nextInt(savedPosts.size()));

      for (Vote vote : post.getVotes()) {
        if (vote.getUser().getId().equals((long) randomUserId)) {
          continue A;
        }
      }

      Vote vote = Vote.from(random.nextBoolean(), savedUsers.get(randomUserId), post);
      votes.add(vote);
    }
    savedVotes = voteRepository.saveAll(votes);
  }
}
