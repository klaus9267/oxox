package kimandhong.oxox.controller;

import jakarta.validation.Valid;
import kimandhong.oxox.dto.profile.ProfileDto;
import kimandhong.oxox.dto.profile.UpdateProfileDto;
import kimandhong.oxox.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {
  private final ProfileService profileService;

  @PatchMapping
  public ResponseEntity<ProfileDto> updateProfile(@RequestBody @Valid final UpdateProfileDto updateProfileDto) {
    final ProfileDto profileDto = profileService.updateProfile(updateProfileDto);
    return ResponseEntity.ok(profileDto);
  }
}
