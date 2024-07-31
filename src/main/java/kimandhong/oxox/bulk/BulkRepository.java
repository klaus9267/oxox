package kimandhong.oxox.bulk;

import kimandhong.oxox.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Repository
@RequiredArgsConstructor
@Transactional
public class BulkRepository {
  private final JdbcTemplate jdbcTemplate;

  public void saveUsers(final List<User> users) {
    String userSql = "INSERT INTO users (email, password) values (?, ?)";
    String profileSql = "INSERT INTO profiles (nickname, sequence, user_id) values (?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    for (User user : users) {
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getPassword());
        return ps;
      }, keyHolder);

      Number userId = keyHolder.getKey();

      jdbcTemplate.update(profileSql,
          user.getProfile().getNickname(),
          user.getProfile().getSequence(),
          userId
      );
    }
  }

  public void savePosts(final List<Post> posts) {
    String sql = "INSERT INTO posts (title, content, user_id, is_done,created_at) values (?, ?, ?, ?, ?)";
    Random random = new Random();

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
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
    String sql = "INSERT INTO votes (is_yes, user_id, post_id) values (?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
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
    String sql = "INSERT INTO comments (content, user_id, post_id) values (?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
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
    String sql = "INSERT INTO reactions (emoji, user_id, comment_id) values (?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
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
    String sql = "DELETE FROM votes";

    jdbcTemplate.batchUpdate(sql);
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
        "SET FOREIGN_KEY_CHECKS = 0",
        "DELETE FROM reactions",
        "DELETE FROM comments",
        "SET FOREIGN_KEY_CHECKS = 1"
    };

    jdbcTemplate.batchUpdate(sqls);
  }

  public void deletePosts() {
    String[] sqls = {
        "SET FOREIGN_KEY_CHECKS = 0",
        "DELETE FROM votes",
        "DELETE FROM reactions",
        "DELETE FROM comments",
        "DELETE FROM posts",
        "SET FOREIGN_KEY_CHECKS = 1"
    };

    jdbcTemplate.batchUpdate(sqls);
  }

  public void deleteUsers() {
    String[] sqls = {
        "SET FOREIGN_KEY_CHECKS = 0",
        "DELETE FROM votes",
        "DELETE FROM reactions",
        "DELETE FROM comments",
        "DELETE FROM posts",
        "DELETE FROM profiles",
        "DELETE FROM users",
        "SET FOREIGN_KEY_CHECKS = 1"
    };

    jdbcTemplate.batchUpdate(sqls);
  }
}
