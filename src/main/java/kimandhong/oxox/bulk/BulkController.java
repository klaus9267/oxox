package kimandhong.oxox.bulk;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
}
