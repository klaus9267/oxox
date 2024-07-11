package kimandhong.oxox.dto.poll;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public record CreatePollDto(
    @Length(max = 30, message = "제목은 30자 이하여야 합니다.")
    @NotBlank(message = "title is required")
    @Schema(example = "test title")
    String title,
    @Length(max = 255, message = "제목은 255자 이하여야 합니다.")
    @NotBlank(message = "content is required")
    @Schema(example = "test contest")
    String content
) {
}
