package kimandhong.oxox.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record JoinDto(
    @Email
    @NotBlank(message = "email is required")
    @Schema(example = "test@email.com")
    String email,
    @Length(min = 6, max = 20, message = "비밀번호는 20자 이하여야 합니다.")
    @NotBlank(message = "password is required")
    @Schema(example = "test password")
    String password,
    @Length(max = 20, message = "닉네임은 20자 이하여야 합니다.")
    @NotBlank(message = "nickname is required")
    @Schema(example = "test nickname")
    String nickname,
    @Schema(example = "🔔")
    String profileEmoji
) {
}
