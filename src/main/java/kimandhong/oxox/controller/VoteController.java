package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kimandhong.oxox.common.swagger.SwaggerNoContent;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
@Tag(name = "VOTE API")
public class VoteController {
  private final VoteService voteService;

  @PostMapping
  @SwaggerOK(summary = "투표하기")
  public void vote(@RequestParam final Long postId,
                   @RequestParam final boolean isYes) {
    voteService.vote(postId, isYes);
  }

  @DeleteMapping("{postId}")
  @SwaggerNoContent(summary = "투표취소")
  public void deleteVote(@PathVariable("postId") final Long postId) {
    voteService.deleteVote(postId);
  }
}
