package kimandhong.oxox.dto.post;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PostPaginationDto(
    List<PostDto> posts,
    int totalPage,
    long totalElement
) {
  public static PostPaginationDto from(final Page<PostDto> postDtoPage) {
    return PostPaginationDto.builder()
        .posts(postDtoPage.getContent())
        .totalPage(postDtoPage.getTotalPages())
        .totalElement(postDtoPage.getTotalElements())
        .build();
  }
}
