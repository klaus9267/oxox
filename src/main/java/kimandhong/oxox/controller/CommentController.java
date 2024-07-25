package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kimandhong.oxox.common.swagger.SwaggerCreated;
import kimandhong.oxox.common.swagger.SwaggerNoContent;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.controller.param.CommentPaginationParam;
import kimandhong.oxox.dto.comment.CommentPaginationDto;
import kimandhong.oxox.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("{postId}")
  @SwaggerOK(summary = "댓글 목록 조회")
  public ResponseEntity<CommentPaginationDto> paginationComments(@PathVariable("postId") final Long postId,
                                                                 @ParameterObject @Valid final CommentPaginationParam paginationParam) {
    final CommentPaginationDto posts = commentService.readAllCommentsByPostId(paginationParam, postId);
    return ResponseEntity.ok(posts);
  }

  @PatchMapping
  @SwaggerNoContent(summary = "댓글 수정")
  public void updateComment(@RequestParam("commentId") final Long commentId,
                            @RequestParam("content") final String content) {
    commentService.updateComment(commentId, content);
  }

  @DeleteMapping("{commentId}")
  @SwaggerNoContent(summary = "댓글 삭제")
  public void deleteComment(@PathVariable("commentId") final Long commentId) {
    commentService.deleteComment(commentId);
  }
}
