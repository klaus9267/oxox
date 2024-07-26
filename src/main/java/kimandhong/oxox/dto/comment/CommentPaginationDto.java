package kimandhong.oxox.dto.comment;

import kimandhong.oxox.domain.Comment;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record CommentPaginationDto(
    List<CommentDto> comments,
    int totalPage,
    long totalElement
) {
  public static CommentPaginationDto from(final Page<Comment> commentPage, final Long userId) {
    final List<CommentDto> commentDtos = CommentDto.from(commentPage.getContent(), userId);
    return CommentPaginationDto.builder()
        .comments(commentDtos)
        .totalPage(commentPage.getTotalPages())
        .totalElement(commentPage.getTotalElements())
        .build();
  }
}
