package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kimandhong.oxox.common.swagger.SwaggerCreated;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.dto.poll.CreatePollDto;
import kimandhong.oxox.dto.poll.PollDto;
import kimandhong.oxox.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
@Tag(name = "POLL API")
public class PollController {
  private final PollService pollService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @SwaggerCreated(summary = "게시글 생성")
  public ResponseEntity<PollDto> createPoll(@RequestPart @Valid final CreatePollDto createPollDto,
                                            @RequestPart(required = false) final MultipartFile thumbnail) {
    final PollDto pollDto = pollService.createPoll(createPollDto, thumbnail);
    return ResponseEntity.status(HttpStatus.CREATED).body(pollDto);
  }

  @GetMapping("{pollId}")
  @SwaggerOK(summary = "게시글 상세조회")
  public ResponseEntity<PollDto> readPoll(@PathVariable("pollId") final Long pollId) {
    final PollDto pollDto = pollService.readPoll(pollId);
    return ResponseEntity.ok(pollDto);
  }
}
