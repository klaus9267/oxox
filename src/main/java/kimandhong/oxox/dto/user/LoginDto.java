package kimandhong.oxox.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginDto(
    @Email
    @NotBlank(message = "email is required")
    String email,
    @Length(max = 20, message = "비밀번호는 20자 이하여야 합니다.")
    @NotBlank(message = "password is required")
    String password
) {
}
