package kimandhong.oxox.repository.custom;

import jakarta.persistence.EntityManager;
import kimandhong.oxox.common.DataInitializer;
import kimandhong.oxox.config.TestQueryDslConfig;
import kimandhong.oxox.controller.param.PostCondition;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.dto.post.PostDto;
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
}