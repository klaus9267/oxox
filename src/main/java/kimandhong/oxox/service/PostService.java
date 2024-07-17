package kimandhong.oxox.service;

import kimandhong.oxox.auth.SecurityUtil;
import kimandhong.oxox.controller.param.SortType;
import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.post.CreatePostDto;
import kimandhong.oxox.dto.post.PostDetailDto;
import kimandhong.oxox.dto.post.PostDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.CommentRepository;
import kimandhong.oxox.repository.PostRepository;
import kimandhong.oxox.repository.querydsl.PostCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final PostCustomRepository postCustomRepository;
  private final CommentRepository commentRepository;

  private final SecurityUtil securityUtil;
  private final S3Service s3Service;

  @Transactional
  public PostDto createPost(final CreatePostDto createPostDto, final MultipartFile thumbnail) {
    final User user = securityUtil.getCurrentUser();
    final String thumbnailUrl = thumbnail != null ? s3Service.uploadThumbnail(thumbnail) : null;

    try {
      final Post post = Post.from(createPostDto, user, thumbnailUrl);
      final Post savedPost = postRepository.save(post);

      return PostDto.from(savedPost);
    } catch (Exception e) {
      s3Service.deleteThumbnail(thumbnailUrl);
      throw new RuntimeException(e.getMessage());
    }
  }

  public PostDetailDto readPost(final Long id) {
    final Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
    final List<Comment> comments = commentRepository.findAllByPostId(id);
    return PostDetailDto.from(post, comments);
  }

  public Post findById(final Long id) {
    return postRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
  }

  public List<PostDto> readAllPosts(final SortType sortType) {
    List<Post> posts = SortType.WRITER.equals(sortType) || SortType.JOIN.equals(sortType)
        ? postCustomRepository.findAllSortedWithUserId(sortType, securityUtil.getCustomUserId())
        : postCustomRepository.findAllSorted(sortType);

    return PostDto.from(posts);
  }

  @Transactional
  public void deletePost(final Long id) {
    final Post post = postRepository.findByIdAndUserId(id, securityUtil.getCustomUserId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
    postRepository.deleteById(post.getId());
    s3Service.deleteThumbnail(post.getThumbnail());
  }

  @Transactional
  //todo will change to message queue
  @Scheduled(fixedDelay = 1000 * 60 * 30)
  public void checkPostIsDOne() {
    postRepository.findAll().forEach(post -> {
      if (!post.isDone() && post.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
        post.done();
      }
    });
  }
}
