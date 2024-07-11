package kimandhong.oxox.controller;

import kimandhong.oxox.dto.poll.PollDto;
import kimandhong.oxox.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
public class PollController {
  private final PollService pollService;

  @PostMapping
  public ResponseEntity<PollDto> createPoll(@RequestParam("title") final String title,
                                            @RequestParam("content") final String content,
                                            @RequestPart final MultipartFile thumbnail) {
    final PollDto pollDto = pollService.createPoll(title, content, thumbnail);
    return ResponseEntity.status(HttpStatus.CREATED).body(pollDto);
  }

  @GetMapping("{pollId}")
  public ResponseEntity<PollDto> readPoll(@PathVariable("pollId") final Long pollId) {
    final PollDto pollDto = pollService.readPoll(pollId);
    return ResponseEntity.ok(pollDto);
  }
}
