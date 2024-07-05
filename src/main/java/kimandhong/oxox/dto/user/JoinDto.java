package kimandhong.oxox.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record JoinDto(
    @Email
    String email,
    @Max(20)
    @NotBlank(message = "password is required")
    String password,
    @Max(20)
    @NotBlank(message = "nickname is required")
    String nickname
) {
}
