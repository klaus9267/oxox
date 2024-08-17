package kimandhong.oxox.application.bulk;

import kimandhong.oxox.domain.comment.domain.Comment;
import kimandhong.oxox.domain.post.domain.Post;
import kimandhong.oxox.domain.reaction.domain.Reaction;
import kimandhong.oxox.domain.profile.Profile;
import kimandhong.oxox.domain.user.domain.User;
import kimandhong.oxox.domain.vote.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class BulkRepository {
  private final JdbcTemplate jdbcTemplate;
  private final SecureRandom random = new SecureRandom();

  public void saveUsers(final List<User> users) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    for (User user : users) {
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(Sql.INSERT_USER.getQuery(), Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getPassword());
        return ps;
      }, keyHolder);

      Number userId = keyHolder.getKey();

      jdbcTemplate.update(Sql.INSERT_PROFILE.getQuery(),
          user.getProfile().getNickname(),
          user.getProfile().getSequence(),
          userId
      );
    }
  }

  public void savePosts(final List<Post> posts) {
    jdbcTemplate.batchUpdate(Sql.INSERT_POST.getQuery(), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        Post post = posts.get(i);
        ps.setString(1, post.getTitle());
        ps.setString(2, post.getContent());
        ps.setLong(3, post.getUser().getId());
        ps.setBoolean(4, false);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now().minusHours(random.nextInt(48))));
      }

      @Override
      public int getBatchSize() {
        return posts.size();
      }
    });
  }

  public void saveVotes(final List<Vote> votes) {
    jdbcTemplate.batchUpdate(Sql.INSERT_VOTE.getQuery(), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        Vote vote = votes.get(i);
        ps.setBoolean(1, vote.isYes());
        ps.setLong(2, vote.getUser().getId());
        ps.setLong(3, vote.getPost().getId());
      }

      @Override
      public int getBatchSize() {
        return votes.size();
      }
    });
  }

  public void saveComments(final List<Comment> comments) {
    jdbcTemplate.batchUpdate(Sql.INSERT_COMMENT.getQuery(), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        Comment comment = comments.get(i);
        ps.setString(1, comment.getContent());
        ps.setLong(2, comment.getUser().getId());
        ps.setLong(3, comment.getPost().getId());
      }

      @Override
      public int getBatchSize() {
        return comments.size();
      }
    });
  }

  public void saveReactions(final List<Reaction> reactions) {
    jdbcTemplate.batchUpdate(Sql.INSERT_REACTION.getQuery(), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        Reaction reaction = reactions.get(i);
        ps.setObject(1, reaction.getEmoji().getName());
        ps.setLong(2, reaction.getUser().getId());
        ps.setLong(3, reaction.getComment().getId());
      }

      @Override
      public int getBatchSize() {
        return reactions.size();
      }
    });
  }

  public void deleteVotes() {
    jdbcTemplate.batchUpdate(Sql.DELETE_VOTE.getQuery());
  }

  public void deleteReactions() {
    String[] sql = {
        "DELETE FROM reactions",
        "DELETE FROM reaction_counts"
    };

    jdbcTemplate.batchUpdate(sql);
  }

  public void deleteComments() {
    String[] sqls = {
        Sql.KEY_0.getQuery(),
        Sql.DELETE_REACTION.getQuery(),
        Sql.DELETE_REACTION_COUNT.getQuery(),
        Sql.DELETE_COMMENT.getQuery(),
        Sql.KEY_1.getQuery(),
    };

    jdbcTemplate.batchUpdate(sqls);
  }

  public void deletePosts() {
    String[] sqls = {
        Sql.KEY_0.getQuery(),
        Sql.DELETE_VOTE.getQuery(),
        Sql.DELETE_REACTION.getQuery(),
        Sql.DELETE_REACTION_COUNT.getQuery(),
        Sql.DELETE_COMMENT.getQuery(),
        Sql.DELETE_POST.getQuery(),
        Sql.KEY_1.getQuery()
    };

    jdbcTemplate.batchUpdate(sqls);
  }

  public void deleteUsers() {
    String[] sqls = {
        Sql.KEY_0.getQuery(),
        Sql.DELETE_VOTE.getQuery(),
        Sql.DELETE_REACTION.getQuery(),
        Sql.DELETE_REACTION_COUNT.getQuery(),
        Sql.DELETE_COMMENT.getQuery(),
        Sql.DELETE_POST.getQuery(),
        Sql.DELETE_PROFILE.getQuery(),
        Sql.DELETE_USER.getQuery(),
        Sql.KEY_1.getQuery(),
    };

    jdbcTemplate.batchUpdate(sqls);
  }

  public void updateProfileSequences(final List<Profile> profiles) {
    jdbcTemplate.batchUpdate(Sql.UPDATE_PROFILE.getQuery(), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        Profile profile = profiles.get(i);
        ps.setLong(1, profile.getSequence());
        ps.setLong(2, profile.getId());
      }

      @Override
      public int getBatchSize() {
        return profiles.size();
      }
    });
  }

  public void donePosts(final List<Post> posts) {
    jdbcTemplate.batchUpdate(Sql.UPDATE_POST.getQuery(), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        Post post = posts.get(i);
        ps.setLong(1, post.getId());
      }

      @Override
      public int getBatchSize() {
        return posts.size();
      }
    });
  }
}