package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kimandhong.oxox.common.swagger.SwaggerCreated;
import kimandhong.oxox.common.swagger.SwaggerNoContent;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.controller.param.SortType;
import kimandhong.oxox.dto.post.CreatePostDto;
import kimandhong.oxox.dto.post.PostDetailDto;
import kimandhong.oxox.dto.post.PostDto;
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

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @SwaggerCreated(summary = "게시글 생성")
  public ResponseEntity<PostDto> createPost(@ParameterObject @Valid final CreatePostDto createPostDto,
                                            @RequestPart(required = false) final MultipartFile thumbnail) {
    final PostDto postDto = postService.createPost(createPostDto, thumbnail);
    return ResponseEntity.status(HttpStatus.CREATED).body(postDto);
  }

  @GetMapping("{postId}")
  @SwaggerOK(summary = "게시글 상세조회")
  public ResponseEntity<PostDetailDto> readPost(@PathVariable("postId") final Long postId) {
    final PostDetailDto postDto = postService.readPost(postId);
    return ResponseEntity.ok(postDto);
  }

  @GetMapping
  @SwaggerOK(summary = "게시글 목록 조회", description = "join, writier은 로그인 필요")
  public ResponseEntity<List<PostDto>> paginationPosts(@RequestParam @Valid final SortType sortType) {
    final List<PostDto> posts = postService.readAllPosts(sortType);
    return ResponseEntity.ok(posts);
  }

  @DeleteMapping("{postId}")
  @SwaggerNoContent(summary = "게시글 삭제")
  public void deletePost(@PathVariable("postId") final Long postId) {
    postService.deletePost(postId);
  }
}
