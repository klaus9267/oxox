package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import kimandhong.oxox.common.swagger.SwaggerOK;
import kimandhong.oxox.dto.profile.ProfileDto;
import kimandhong.oxox.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "PROFILE API")
@Validated
public class ProfileController {
  private final ProfileService profileService;

  @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @SwaggerOK(summary = "프로필 수정")
  public ResponseEntity<ProfileDto> updateProfile(@RequestParam @Size(max = 20, min = 1, message = "nickname은 20자 이하여야합니다.") final String nickname,
                                                  @RequestPart(required = false) final MultipartFile image) {
    final ProfileDto profileDto = profileService.updateProfile(nickname, image);
    return ResponseEntity.ok(profileDto);
  }
}
