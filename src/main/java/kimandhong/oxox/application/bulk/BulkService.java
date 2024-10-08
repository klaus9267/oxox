package kimandhong.oxox.application.bulk;

import kimandhong.oxox.domain.comment.domain.Comment;
import kimandhong.oxox.domain.post.domain.Post;
import kimandhong.oxox.domain.reaction.domain.Emoji;
import kimandhong.oxox.domain.reaction.domain.Reaction;
import kimandhong.oxox.domain.user.domain.User;
import kimandhong.oxox.domain.vote.Vote;
import kimandhong.oxox.domain.post.dto.RequestPostDto;
import kimandhong.oxox.domain.user.dto.JoinDto;
import kimandhong.oxox.domain.comment.CommentRepository;
import kimandhong.oxox.domain.post.repository.PostRepository;
import kimandhong.oxox.domain.user.UserRepository;
import kimandhong.oxox.domain.profile.repository.ProfileCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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

  private final SecureRandom random = new SecureRandom();

  @Value("${bulk.password}")
  private String password;

  public void bulkUsers() {
    List<User> users = new ArrayList<>();
    String encodedPassword = passwordEncoder.encode(password);
    for (int i = 0; i < 1000; i++) {
      JoinDto joinDto = new JoinDto("test" + random.nextInt(9999) + "@email.com", null, "bulk nickname" + random.nextInt(9999));

      long sequence = users.stream()
          .filter(user -> user.getProfile().getNickname().equals(joinDto.nickname()))
          .mapToLong(user -> user.getProfile().getSequence())
          .max()
          .orElse(0L);

      long dbSequence = profileCustomRepository.findMaxSequenceByNickname(joinDto.nickname());

      User user = User.from(joinDto, encodedPassword, Math.max(sequence, dbSequence) + 1, null);
      users.add(user);
    }
    bulkRepository.saveUsers(users);
  }

  public void deleteAllUsers() {
    bulkRepository.deleteUsers();
  }

  public void bulkPosts() {
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
    List<User> users = userRepository.findAll();
    List<Post> posts = postRepository.findAll();
    List<Vote> votes = new ArrayList<>();

    for (int i = 0; i < 10000; i++) {
      int randomUserId = random.nextInt(users.size());
      int randomPostId = random.nextInt(posts.size());

      Post randomPost = posts.get(randomPostId);
      boolean isExist = randomPost.getOneToMany().getVotes().stream()
          .anyMatch(vote -> vote.getUser().getId().equals((long) randomUserId));

      if (!isExist) {
        Vote vote = Vote.from(random.nextBoolean(), users.get(randomUserId), posts.get(randomPostId));
        votes.add(vote);
      }
    }
    bulkRepository.saveVotes(votes);
  }

  public void deleteAllVotes() {
    bulkRepository.deleteVotes();
  }

  public void bulkReactions() {
    List<User> users = userRepository.findAll();
    List<Comment> comments = commentRepository.findAll();
    List<Reaction> reactions = new ArrayList<>();

    for (int i = 0; i < 10000; i++) {
      int randomUserId = random.nextInt(users.size());
      int randomCommentId = random.nextInt(comments.size());
      int randomEmoji = random.nextInt(Emoji.values().length - 1);
      Comment randomComment = comments.get(randomCommentId);

      boolean isExist = randomComment.getOneToMany().getReactions().stream()
          .anyMatch(reaction -> reaction.getUser().getId().equals(users.get(randomUserId).getId()));
      if (!isExist) {
        Reaction reaction = Reaction.from(Emoji.values()[randomEmoji], users.get(randomUserId), randomComment);
        reactions.add(reaction);
        randomComment.incrementCount(Emoji.values()[randomEmoji]);
      }
    }
    bulkRepository.saveReactions(reactions);
  }

  public void deleteAllReactions() {
    bulkRepository.deleteReactions();
  }
}
