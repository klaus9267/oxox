package kimandhong.oxox.service;

import kimandhong.oxox.controller.param.PostPaginationParam;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.post.CreatePostDto;
import kimandhong.oxox.dto.post.PostDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final UserService userService;
  private final S3Service s3Service;

  @Transactional
  public PostDto createPost(final CreatePostDto createPostDto, final MultipartFile thumbnail) {
    final User user = userService.findCurrentUser();
    final String thumbnailUrl = thumbnail != null ? s3Service.uploadThumbnail(thumbnail) : null;

    try {
      final Post post = Post.from(createPostDto.title(), createPostDto.content(), user, thumbnailUrl);
      final Post savedPost = postRepository.save(post);

      return PostDto.from(savedPost);
    } catch (Exception e) {
      s3Service.deleteThumbnail(thumbnailUrl);
      throw new RuntimeException(e.getMessage());
    }
  }

  public PostDto readPost(final Long id) {
    final Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
    return PostDto.from(post);
  }

  public List<PostDto> readAll() {
    final List<Post> posts = postRepository.findAll();
    return PostDto.from(posts);
  }

  @Transactional
  public void deletePost(final Long id) {
    final Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
    s3Service.deleteThumbnail(post.getThumbnail());
    postRepository.deleteById(post.getId());
  }
}
