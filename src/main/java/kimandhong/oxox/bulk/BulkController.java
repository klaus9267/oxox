package kimandhong.oxox.bulk;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bulks")
@Tag(name = "BULK API")
@RequiredArgsConstructor
public class BulkController {
  private final BulkService bulkService;

  @PostMapping("users")
  public String bulkUsers() {
    bulkService.bulkUsers();
    return "success bulk users!";
  }

  @PostMapping("posts")
  public String bulkPosts() {
    bulkService.bulkPosts();
    return "success bulk posts!";
  }

  @PostMapping("comments")
  public String bulkComments() {
    bulkService.bulkComments();
    return "success bulk comments!";
  }

  @PostMapping("votes")
  public String bulkVotes() {
    bulkService.bulkVotes();
    return "success bulk votes!";
  }

  @PostMapping("reactions")
  public String bulkReactions() {
    bulkService.bulkReactions();
    return "success bulk reactions!";
  }

  @DeleteMapping("users")
  public String deleteAllUsers() {
    bulkService.deleteAllUsers();
    return "success delete all users!";
  }

  @DeleteMapping("posts")
  public String deleteAllPosts() {
    bulkService.deleteAllPosts();
    return "success delete all posts!";
  }

  @DeleteMapping("comments")
  public String deleteAllComments() {
    bulkService.deleteAllComments();
    return "success delete all comments!";
  }

  @DeleteMapping("votes")
  public String deleteAllVotes() {
    bulkService.deleteAllVotes();
    return "success delete all votes!";
  }

  @DeleteMapping("reactions")
  public String deleteAllReactions() {
    bulkService.deleteAllReactions();
    return "success delete all reactions!";
  }
}