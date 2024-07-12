package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
@Tag(name = "VOTE API")
public class VoteController {
  private final VoteService voteService;

  @PostMapping
  @SwaggerOK(summary = "투표하기", description = "isYes = null 이면 투표 취소")
  public void vote(@RequestParam final Long postId,
                   @RequestParam(required = false) final Boolean isYes) {
    voteService.vote(postId, isYes);
  }
}
