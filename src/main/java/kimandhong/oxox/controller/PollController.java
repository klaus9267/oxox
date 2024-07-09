package kimandhong.oxox.controller;

import jakarta.validation.Valid;
import kimandhong.oxox.dto.poll.CreatePollDto;
import kimandhong.oxox.dto.poll.PollDto;
import kimandhong.oxox.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
public class PollController {
  private final PollService pollService;

  @PostMapping
  public ResponseEntity<PollDto> createPoll(@Valid final CreatePollDto createPollDto) {
    final PollDto pollDto = pollService.createPoll(createPollDto);
    return ResponseEntity.ok(pollDto);
  }

  @GetMapping("{pollId}")
  public ResponseEntity<PollDto> readPoll(@PathVariable("pollId") final Long pollId) {
    final PollDto pollDto = pollService.readPoll(pollId);
    return ResponseEntity.ok(pollDto);
  }
}
