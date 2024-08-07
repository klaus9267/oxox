package kimandhong.oxox.service;

import kimandhong.oxox.auth.SecurityUtil;
import kimandhong.oxox.controller.param.CommentPaginationParam;
import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.dto.comment.CommentDto;
import kimandhong.oxox.dto.comment.CommentPaginationDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final PostService postService;
  private final SecurityUtil securityUtil;

  @Transactional
  public void createComment(final Long postId, final String content) {
    final Post post = postService.findById(postId);
    final Comment comment = Comment.from(content, securityUtil.getCurrentUser(), post);
    post.getOneToMany().getComments().add(comment);
  }

  public Comment findById(final Long commentId) {
    return commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMENT));
  }

  public CommentPaginationDto readAllCommentsByPostId(final CommentPaginationParam paginationParam, final Long postId) {
    final Page<Comment> commentPage = commentRepository.findAllByPostId(postId, paginationParam.toPageable());
    return CommentPaginationDto.from(commentPage, securityUtil.getCustomUserId());
  }

  public List<CommentDto> readCommentsByPostId(final Long postId) {
    final List<Comment> comments = commentRepository.findAllByPostId(postId);
    return CommentDto.from(comments, securityUtil.getCustomUserId());
  }

  @Transactional
  public void updateComment(final Long commentId, final String content) {
    final Comment comment = commentRepository.findByIdAndUserId(commentId, securityUtil.getCustomUserId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMENT));
    comment.updateContent(content);
  }

  @Transactional
  public void deleteComment(final Long commentId) {
    final Comment comment = commentRepository.findByIdAndUserId(commentId, securityUtil.getCustomUserId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMENT));
    commentRepository.delete(comment);
  }
}
