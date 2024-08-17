package kimandhong.oxox.domain.reaction;

import io.swagger.v3.oas.annotations.tags.Tag;
import kimandhong.oxox.domain.common.swagger.SwaggerOK;
import kimandhong.oxox.domain.reaction.domain.Emoji;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
@Tag(name = "REACTION API")
public class ReactionController {
  private final ReactionService reactionService;

  @PostMapping
  @SwaggerOK(summary = "댓글에 반응하기(생성, 수정, 삭제)", description = "이미 반응했던 댓글에 다시 요청을 보내면 반응 수정 /emoji 미입력 시 반응 삭제")
  public void react(@RequestParam("commentId") final Long commentId,
                    @RequestParam(value = "emoji", required = false) final Emoji emoji) {
    reactionService.react(commentId, emoji);
  }
}
