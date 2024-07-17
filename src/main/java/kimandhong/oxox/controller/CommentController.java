package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kimandhong.oxox.common.swagger.SwaggerCreated;
import kimandhong.oxox.common.swagger.SwaggerNoContent;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.dto.comment.CommentDto;
import kimandhong.oxox.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "COMMENT API")
public class CommentController {
  private final CommentService commentService;

  @PostMapping
  @SwaggerCreated(summary = "댓글 생성")
  public void createComment(@RequestParam("postId") final Long postId,
                            @RequestParam("content") final String content) {
    commentService.createComment(postId, content);
  }

  @PatchMapping
  @SwaggerNoContent(summary = "댓글 수정")
  public void UpdateComment(@RequestParam("commentId") final Long commentId,
                            @RequestParam("content") final String content) {
    commentService.updateComment(commentId, content);
  }

  @DeleteMapping("{commentId}")
  @SwaggerNoContent(summary = "댓글 삭제")
  public void deleteComment(@PathVariable("commentId") final Long commentId) {
    commentService.deleteComment(commentId);
  }
}
