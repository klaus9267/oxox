package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kimandhong.oxox.common.swagger.SwaggerCreated;
import kimandhong.oxox.common.swagger.SwaggerNoContent;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.controller.param.PostPaginationParam;
import kimandhong.oxox.dto.post.CreatePostDto;
import kimandhong.oxox.dto.post.PostDto;
import kimandhong.oxox.dto.post.PostPagination;
import kimandhong.oxox.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "POST API")
public class PostController {
  private final PostService postService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @SwaggerCreated(summary = "게시글 생성")
  public ResponseEntity<PostDto> createPost(@RequestPart @Valid final CreatePostDto createPostDto,
                                            @RequestPart(required = false) final MultipartFile thumbnail) {
    final PostDto postDto = postService.createPost(createPostDto, thumbnail);
    return ResponseEntity.status(HttpStatus.CREATED).body(postDto);
  }

  @GetMapping("{postId}")
  @SwaggerOK(summary = "게시글 상세조회", description = "양측 투표 수 추가 예정")
  public ResponseEntity<PostDto> readPost(@PathVariable("postId") final Long postId) {
    final PostDto postDto = postService.readPost(postId);
    return ResponseEntity.ok(postDto);
  }

  @GetMapping("develop")
  @SwaggerOK(summary = "게시글 전체 조회(개발용)")
  public ResponseEntity<List<PostDto>> readAll() {
    final List<PostDto> postDtos = postService.readAll();
    return ResponseEntity.ok(postDtos);
  }

  @GetMapping
  @SwaggerOK(summary = "게시글 페이지네이션")
  public ResponseEntity<PostPagination> paginationPosts(@ParameterObject @Valid final PostPaginationParam postPaginationParam) {
    final PostPagination postPagination = postService.readAllWithPagination(postPaginationParam);
    return ResponseEntity.ok(postPagination);
  }

  @DeleteMapping("{postId}")
  @SwaggerNoContent(summary = "게시글 삭제")
  public void deletePost(@PathVariable("postId") final Long postId) {
    postService.deletePost(postId);
  }
}
