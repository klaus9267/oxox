package kimandhong.oxox.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record JoinDto(
    @Email
    @NotBlank(message = "email is required")
    String email,
    @Length(min = 6, max = 20, message = "비밀번호는 20자 이하여야 합니다.")
    @NotBlank(message = "password is required")
    String password,
    @Length(max = 20, message = "닉네임은 20자 이하여야 합니다.")
    @NotBlank(message = "nickname is required")
    String nickname,
    @NotBlank(message = "emoji is required")
    String profileEmoji
) {
}
