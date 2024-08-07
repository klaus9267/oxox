package kimandhong.oxox.bulk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sql {
  INSERT_USER("INSERT INTO users (email, password) values (?, ?)"),
  INSERT_PROFILE("INSERT INTO profiles (nickname, sequence, user_id) values (?, ?, ?)"),
  INSERT_POST("INSERT INTO posts (title, content, user_id, is_done,created_at) values (?, ?, ?, ?, ?)"),
  INSERT_VOTE("INSERT INTO votes (is_yes, user_id, post_id) values (?, ?, ?)"),
  INSERT_COMMENT("INSERT INTO votes (is_yes, user_id, post_id) values (?, ?, ?)"),
  INSERT_REACTION("INSERT INTO reactions (emoji, user_id, comment_id) values (?, ?, ?)"),

  UPDATE_PROFILE( "UPDATE profiles SET sequence = ? where id = ?"),
  UPDATE_POST(  "UPDATE posts SET is_done = true where id = ?"),

  DELETE_VOTE("DELETE FROM votes"),
  DELETE_REACTION("DELETE FROM reactions"),
  DELETE_REACTION_COUNT("DELETE FROM reaction_counts"),
  DELETE_COMMENT("DELETE FROM comments"),
  DELETE_POST("DELETE FROM posts"),
  DELETE_PROFILE("DELETE FROM profiles"),
  DELETE_USER( "DELETE FROM users"),

  KEY_0("SET FOREIGN_KEY_CHECKS = 0"),
  KEY_1("SET FOREIGN_KEY_CHECKS = 1"),
  ;

  private final String query;
}
