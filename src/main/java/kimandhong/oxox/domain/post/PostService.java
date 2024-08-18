package kimandhong.oxox.domain.post;

import kimandhong.oxox.application.auth.SecurityUtil;
import kimandhong.oxox.application.bulk.BulkRepository;
import kimandhong.oxox.application.s3.S3path;
import kimandhong.oxox.domain.post.domain.Post;
import kimandhong.oxox.domain.post.params.PostCondition;
import kimandhong.oxox.domain.post.params.PostPaginationParam;
import kimandhong.oxox.domain.comment.domain.Comment;
import kimandhong.oxox.domain.post.repository.PostRepository;
import kimandhong.oxox.domain.user.domain.User;
import kimandhong.oxox.domain.post.dto.PostDetailDto;
import kimandhong.oxox.domain.post.dto.PostDto;
import kimandhong.oxox.domain.post.dto.PostPaginationDto;
import kimandhong.oxox.domain.post.dto.RequestPostDto;
import kimandhong.oxox.application.handler.error.ErrorCode;
import kimandhong.oxox.application.handler.error.exception.NotFoundException;
import kimandhong.oxox.application.handler.error.exception.S3Exception;
import kimandhong.oxox.domain.comment.CommentRepository;
import kimandhong.oxox.domain.post.repository.PostCustomRepository;
import kimandhong.oxox.application.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
  private final PostRepository postRepository;
  private final PostCustomRepository postCustomRepository;
  private final CommentRepository commentRepository;
  private final BulkRepository bulkRepository;

  private final SecurityUtil securityUtil;
  private final S3Service s3Service;

  @Transactional
  public void createPost(final RequestPostDto requestPostDto, final MultipartFile thumbnail) {
    final User user = securityUtil.getCurrentUser();
    final String thumbnailUrl = thumbnail != null ? s3Service.uploadFile(thumbnail, S3path.THUMBNAIL) : null;

    try {
      final Post post = Post.from(requestPostDto, user, thumbnailUrl);
      postRepository.save(post);
    } catch (Exception e) {
      s3Service.deleteFile(thumbnailUrl);
      throw new S3Exception(ErrorCode.S3_UPLOAD_FAIL);
    }
  }

  public PostDetailDto readPost(final Long id) {
    final Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
    final List<Comment> comments = commentRepository.findAllByPostId(id);
    return securityUtil.isLogin()
        ? PostDetailDto.from(post, comments)
        : PostDetailDto.from(post, comments, securityUtil.getCustomUserId());
  }

  public Post findById(final Long id) {
    return postRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
  }

  public PostPaginationDto readAllPosts(final PostPaginationParam paginationParam) {
    final PostCondition postCondition = paginationParam.condition();

    Page<PostDto> posts = switch (postCondition) {
      case WRITER, JOIN -> postCustomRepository.findAllWithUserId(postCondition, paginationParam.toPageable(), securityUtil.getCustomUserId());
      default -> postCustomRepository.findAll(postCondition, paginationParam.toPageable());
    };

    return PostPaginationDto.from(posts);
  }

  @Transactional
  public void updatePost(final Long postId, final RequestPostDto postDto, final MultipartFile file) {
    Post post = this.findById(postId);
    String thumbnail = file != null ? s3Service.changeFile(post.getThumbnail(), file, S3path.THUMBNAIL) : post.getThumbnail();
    post.updatePost(postDto, thumbnail);
  }

  @Transactional
  public void deletePost(final Long id) {
    final Post post = postRepository.findByIdAndUserId(id, securityUtil.getCustomUserId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
    postRepository.deleteById(post.getId());
    if (post.getThumbnail() != null) {
      s3Service.deleteFile(post.getThumbnail());
    }
  }

  @Transactional
  //todo: will change to message queue
  @Scheduled(fixedDelay = 1000 * 60 * 30)
  public void updateExpiredPosts() {
    final LocalDateTime time = LocalDateTime.now().minusDays(1);
    final List<Post> posts = postRepository.findByIsDoneFalseAndCreatedAtBefore(time);
    bulkRepository.donePosts(posts);
    log.info("done check");
  }

  @Transactional
  @Scheduled(fixedDelay = 1000 * 60 * 5)
  public void fillPosts() {
    final List<Post> posts = postRepository.findTop5ByOrderByCreatedAtDesc();
    for (Post post : posts) {
      if (post.isDone()) {
        post.resetCreatedAt();
      }
    }
  }
}
