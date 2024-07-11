package kimandhong.oxox.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateProfileDto(
    @Length(max = 20, message = "닉네임은 20자 이하여야 합니다.")
    @NotBlank(message = "nickname is required")
    @Schema(example = "test nickname")
    String nickname,
    @NotBlank(message = "emoji is required")
    @Schema(example = "🎈")
    String emoji
) {
}
