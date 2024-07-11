package kimandhong.oxox.dto.profile;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateProfileDto(
    @Length(max = 20, message = "닉네임은 20자 이하여야 합니다.")
    @NotBlank(message = "nickname is required")
    String nickname,
    @NotBlank(message = "emoji is required")
    String emoji
) {
}
