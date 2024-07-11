package kimandhong.oxox.service;

import kimandhong.oxox.domain.Poll;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.poll.PollDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PollService {
  private final PollRepository pollRepository;
  private final UserService userService;
  private final S3Service s3Service;

  @Transactional
  public PollDto createPoll(final String title, final String content, final MultipartFile thumbnail) {
    final User user = userService.findCurrentUser();
    final String thumbnailUrl = thumbnail != null ? s3Service.uploadThumbnail(thumbnail) : null;

    try {
      final Poll poll = Poll.from(title, content, user, thumbnailUrl);
      final Poll savedPoll = pollRepository.save(poll);

      return PollDto.from(savedPoll);
    } catch (Exception e) {
      s3Service.deleteThumbnail(thumbnailUrl);
      throw new RuntimeException(e.getMessage());
    }
  }

  public PollDto readPoll(final Long id) {
    final Poll poll = pollRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POLL));
    return PollDto.from(poll);
  }

  @Transactional
  public void deletePoll(final Long id) {
    final Poll poll = pollRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POLL));
    s3Service.deleteThumbnail(poll.getThumbnail());
    pollRepository.deleteById(poll.getId());
  }
}
