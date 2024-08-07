package kimandhong.oxox.bulk;

import kimandhong.oxox.domain.*;
import kimandhong.oxox.dto.post.RequestPostDto;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.repository.CommentRepository;
import kimandhong.oxox.repository.PostRepository;
import kimandhong.oxox.repository.UserRepository;
import kimandhong.oxox.repository.custom.ProfileCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class BulkService {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;
  private final ProfileCustomRepository profileCustomRepository;

  private final PasswordEncoder passwordEncoder;
  private final BulkRepository bulkRepository;

  public void bulkUsers() {
    List<User> users = new ArrayList<>();
    String password = passwordEncoder.encode("test password");
    Random random = new Random();
    for (int i = 0; i < 1000; i++) {
      JoinDto joinDto = new JoinDto("test" + random.nextInt(9999) + "@email.com", null, "bulk nickname" + random.nextInt(9999));

      long sequence = users.stream()
          .filter(user -> user.getProfile().getNickname().equals(joinDto.nickname()))
          .mapToLong(user -> user.getProfile().getSequence())
          .max()
          .orElse(0L);

      long dbSequence = profileCustomRepository.findMaxSequenceByNickname(joinDto.nickname());

      User user = User.from(joinDto, password, Math.max(sequence, dbSequence) + 1, null);
      users.add(user);
    }
    bulkRepository.saveUsers(users);
  }

  public void deleteAllUsers() {
    bulkRepository.deleteUsers();
  }

  public void bulkPosts() {
    Random random = new Random();
    List<User> users = userRepository.findAll();
    List<Post> posts = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      int randomUserId = random.nextInt(users.size());
      RequestPostDto requestPostDto = new RequestPostDto("title" + random.nextInt(99999), "content" + random.nextInt(99999));
      Post post = Post.from(requestPostDto, users.get(randomUserId), null);

      posts.add(post);
    }
    bulkRepository.savePosts(posts);
  }

  public void deleteAllPosts() {
    bulkRepository.deletePosts();
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
    bulkRepository.saveComments(comments);
  }

  public void deleteAllComments() {
    bulkRepository.deleteComments();
  }

  public void bulkVotes() {
    Random random = new Random();
    List<User> users = userRepository.findAll();
    List<Post> posts = postRepository.findAll();
    List<Vote> votes = new ArrayList<>();

    A:
    for (int i = 0; i < 10000; i++) {
      int randomUserId = random.nextInt(users.size());
      int randomPostId = random.nextInt(posts.size());

      Post randomPost = posts.get(randomPostId);
      for (Vote vote : randomPost.getOneToMany().getVotes()) {
        if (vote.getUser().getId().equals((long) randomUserId)) {
          continue A;
        }
      }
      Vote vote = Vote.from(random.nextBoolean(), users.get(randomUserId), posts.get(randomPostId));
      votes.add(vote);
    }
    bulkRepository.saveVotes(votes);
  }

  public void deleteAllVotes() {
    bulkRepository.deleteVotes();
  }

  public void bulkReactions() {
    Random random = new Random();
    List<User> users = userRepository.findAll();
    List<Comment> comments = commentRepository.findAll();
    List<Reaction> reactions = new ArrayList<>();

    A:
    for (int i = 0; i < 10000; i++) {
      int randomUserId = random.nextInt(users.size());
      int randomCommentId = random.nextInt(comments.size());
      int randomEmoji = random.nextInt(Emoji.values().length - 1);
      Comment randomComment = comments.get(randomCommentId);

      for (Reaction reaction : randomComment.getOneToMany().getReactions()) {
        if (reaction.getUser().getId().equals(users.get(randomUserId).getId())) {
          continue A;
        }
      }

      Reaction reaction = Reaction.from(Emoji.values()[randomEmoji], users.get(randomUserId), randomComment);
      reactions.add(reaction);
      randomComment.incrementCount(Emoji.values()[randomEmoji]);
    }
    bulkRepository.saveReactions(reactions);
  }

  public void deleteAllReactions() {
    bulkRepository.deleteReactions();
  }
}
