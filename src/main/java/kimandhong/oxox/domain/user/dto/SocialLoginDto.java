package kimandhong.oxox.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SocialLoginDto(
    @Email
    @NotBlank(message = "email is required")
    @Schema(example = "social@email.com")
    String email,
    @NotBlank(message = "displayName is required")
    @Schema(example = "김민호")
    String displayName,
    String photoUrl,
    @NotBlank(message = "uid is required")
    @Schema(example = "test uid")
    String uid
) {
}
