package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.dto.profile.ProfileDto;
import kimandhong.oxox.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "PROFILE API")
public class ProfileController {
  private final ProfileService profileService;

  @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @SwaggerOK(summary = "프로필 수정")
  public ResponseEntity<ProfileDto> updateProfile(@RequestParam @Valid final String nickname,
                                                  @RequestPart(required = false) final MultipartFile image) {
    final ProfileDto profileDto = profileService.updateProfile(nickname, image);
    return ResponseEntity.ok(profileDto);
  }
}
