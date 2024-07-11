package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kimandhong.oxox.common.swagger.SwaggerOK;
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
@Tag(name = "PROFILE API")
public class ProfileController {
  private final ProfileService profileService;

  @PatchMapping
  @SwaggerOK(summary = "프로필 수정",description = "이모지 enum으로 변경 예정")
  public ResponseEntity<ProfileDto> updateProfile(@RequestBody @Valid final UpdateProfileDto updateProfileDto) {
    final ProfileDto profileDto = profileService.updateProfile(updateProfileDto);
    return ResponseEntity.ok(profileDto);
  }
}
