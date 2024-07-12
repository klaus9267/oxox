package kimandhong.oxox.dto.post;

import kimandhong.oxox.domain.Post;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public record PostPagination(
    List<PostDto> posts,
    int totalElement,
    int totalPage,
    boolean hasNest
) {
  public static PostPagination from(final PageImpl<Post> postPage) {
    final List<PostDto> postDtos = PostDto.from(postPage.getContent());
    return new PostPagination(postDtos, postPage.getNumberOfElements(), postPage.getTotalPages(), postPage.hasNext());
  }
}
