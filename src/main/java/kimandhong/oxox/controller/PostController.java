package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kimandhong.oxox.common.swagger.SwaggerCreated;
import kimandhong.oxox.common.swagger.SwaggerNoContent;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.controller.param.PostPaginationParam;
import kimandhong.oxox.dto.post.PostDetailDto;
import kimandhong.oxox.dto.post.PostPaginationDto;
import kimandhong.oxox.dto.post.RequestPostDto;
import kimandhong.oxox.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "POST API")
public class PostController {
  private final PostService postService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @SwaggerCreated(summary = "게시글 생성")
  public void createPost(@ParameterObject @Valid final RequestPostDto requestPostDto,
                         @RequestPart(required = false) final MultipartFile thumbnail) {
    postService.createPost(requestPostDto, thumbnail);
  }

  @GetMapping("{postId}")
  @SwaggerOK(summary = "게시글 상세조회")
  public ResponseEntity<PostDetailDto> readPost(@PathVariable("postId") final Long postId) {
    final PostDetailDto postDto = postService.readPost(postId);
    return ResponseEntity.ok(postDto);
  }

  @GetMapping
  @SwaggerOK(summary = "게시글 목록 조회", description = "join, writer 로그인 필요")
  public ResponseEntity<PostPaginationDto> paginationPosts(@ParameterObject @Valid final PostPaginationParam paginationParam) {
    final PostPaginationDto posts = postService.readAllPosts(paginationParam);
    return ResponseEntity.ok(posts);
  }

  @PatchMapping(value = "{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @SwaggerNoContent(summary = "게시글 수정")
  public void updatePost(@PathVariable("postId") final Long postId,
                         @ParameterObject @Valid final RequestPostDto requestPostDto,
                         @RequestPart(required = false) final MultipartFile thumbnail) {
    postService.updatePost(postId, requestPostDto, thumbnail);
  }

  @DeleteMapping("{postId}")
  @SwaggerNoContent(summary = "게시글 삭제")
  public void deletePost(@PathVariable("postId") final Long postId) {
    postService.deletePost(postId);
  }
}
