package kimandhong.oxox.repository.querydsl;

import jakarta.persistence.EntityManager;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//@ActiveProfiles("test")
class PostCustomRepositoryImplTest {
  @Autowired
  PostRepository postRepository;
  @Autowired
  TestEntityManager testEntityManager;

  EntityManager em;

  @BeforeEach
  void init() {
    em = testEntityManager.getEntityManager();
  }

  @Test
  public void 페이지네이션_BEST_REACTIONS() {
    List<Post> posts = postRepository.findAll();

    List<Integer> reactionCounts = posts.stream()
        .map(post -> post.getComments().stream().map(
                    comment -> comment.getEmojiCounts().values().stream().mapToInt(i -> i).sum()
                ).mapToInt(i -> i)
                .sum()
        ).toList();

    for (int i = 0; i < 9; i++) {
      assertThat(reactionCounts.get(i)).isGreaterThanOrEqualTo(reactionCounts.get(i + 1));
    }
  }
}