package kimandhong.oxox.bulk;

import kimandhong.oxox.domain.*;
import kimandhong.oxox.dto.post.CreatePostDto;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BulkService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;
  private final VoteRepository voteRepository;
  private final ReactionRepository reactionRepository;

  private final PasswordEncoder passwordEncoder;

  public void bulkUsers() {
    List<User> users = new ArrayList<>();
    String password = passwordEncoder.encode("tess password");
    for (int i = 0; i < 1000; i++) {
      JoinDto joinDto = new JoinDto("test" + i + "@email.com", null, "bulk nickname" + i);
      User user = User.from(joinDto, password, 1L, null);
      users.add(user);
    }
    userRepository.saveAll(users);
  }

  public void deleteAllUsers() {
    userRepository.deleteAll();
  }

  public void bulkPosts() {
    Random random = new Random();
    List<User> users = userRepository.findAll();
    List<Post> posts = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      int randomUserId = random.nextInt(users.size());
      CreatePostDto createPostDto = new CreatePostDto("title" + random.nextInt(99999), "content" + random.nextInt(99999));
      Post post = Post.from(createPostDto, users.get(randomUserId), null);
      posts.add(post);
    }

    postRepository.saveAll(posts);
  }

  public void deleteAllPosts() {
    postRepository.deleteAll();
  }

  public void bulkComments() {
    Random random = new Random();
    List<User> users = userRepository.findAll();
    List<Post> posts = postRepository.findAll();
    List<Comment> comments = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      int randomUserId = random.nextInt(users.size());
      int randomPostId = random.nextInt(posts.size());

      Comment comment = Comment.from("content" + random.nextInt(0, 99999), users.get(randomUserId), posts.get(randomPostId));
      comments.add(comment);
    }

    commentRepository.saveAll(comments);
  }

  public void deleteAllComments() {
    commentRepository.deleteAll();
  }

  public void bulkVotes() {
    long startTime = System.currentTimeMillis();
    Random random = new Random();
    List<User> users = userRepository.findAll();
    List<Post> posts = postRepository.findAll();
    List<Vote> votes = new ArrayList<>();

    A:
    for (int i = 0; i < 10000; i++) {
      int randomUserId = random.nextInt(users.size());
      int randomPostId = random.nextInt(posts.size());

      Post randomPost = posts.get(randomPostId);
      for (Vote vote : randomPost.getVotes()) {
        if (vote.getUser().getId().equals((long) randomUserId)) {
          continue A;
        }
      }
      Vote vote = Vote.from(random.nextBoolean(), users.get(randomUserId), posts.get(randomPostId));
      votes.add(vote);
    }

    voteRepository.saveAll(votes);
    long stopTime = System.currentTimeMillis();
    System.out.println(stopTime - startTime);
    // 4466
  }

  public void deleteAllVotes() {
    voteRepository.deleteAll();
  }

  public void bulkReactions() {
    Random random = new Random();
    List<User> users = userRepository.findAll();
    List<Comment> comments = commentRepository.findAll();
    List<Reaction> reactions = new ArrayList<>();

    A:
    for (int i = 0; i < 10000; i++) {
      int randomUserId = random.nextInt(users.size() - 1);
      int randomCommentId = random.nextInt(comments.size() - 1);
      int randomEmoji = random.nextInt(Emoji.values().length - 1);
      Comment randomComment = comments.get(randomCommentId);

      for (Reaction reaction : randomComment.getReactions()) {
        if (reaction.getUser().getId().equals(users.get(randomUserId).getId())) {
          continue A;
        }
      }

      Reaction reaction = Reaction.from(Emoji.values()[randomEmoji], users.get(randomUserId), randomComment);
      reactions.add(reaction);
      randomComment.incrementCount(Emoji.values()[randomEmoji]);
    }

    reactionRepository.saveAll(reactions);
  }

  public void deleteAllReactions() {
    reactionRepository.deleteAll();
  }
}
