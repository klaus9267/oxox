package kimandhong.oxox.repository.querydsl;

import jakarta.persistence.EntityManager;
import kimandhong.oxox.common.DataInitializer;
import kimandhong.oxox.config.TestQueryDslConfig;
import kimandhong.oxox.controller.param.SortType;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestQueryDslConfig.class, DataInitializer.class})
@ActiveProfiles("test")
class PostCustomRepositoryTest {
  @Autowired
  PostRepository postRepository;
  @Autowired
  PostCustomRepository postCustomRepository;
  @Autowired
  TestEntityManager testEntityManager;
  @Autowired
  DataInitializer dataInitializer;
  EntityManager em;

  @BeforeEach
  void init() {
    em = testEntityManager.getEntityManager();
    dataInitializer.init();
  }

  @Test
  public void 게시글_목록_조회_BEST_REACTIONS() {
    dataInitializer.init();
    List<Post> posts = postCustomRepository.findAllSorted(SortType.BEST_REACTION);

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

  @Test
  public void 게시글_목록_조회_POPULARITY() {
    List<Post> posts = postCustomRepository.findAllSorted(SortType.POPULARITY);

    List<Integer> voteCounts = posts.stream()
        .map(post -> post.getVotes().size()
        ).toList();

    for (int i = 0; i < 9; i++) {
      assertThat(voteCounts.get(i)).isGreaterThanOrEqualTo(voteCounts.get(i + 1));
    }
  }
}