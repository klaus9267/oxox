package kimandhong.oxox.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.restdocs.payload.JsonFieldType;

@Getter
@RequiredArgsConstructor
public enum FieldEnum {
  EMAIL("email", JsonFieldType.STRING, "사용자 이메일"),
  PASSWORD("password", JsonFieldType.STRING, "사용자 비밀번호(최대 20자)"),
  EMOJI("profileEmoji", JsonFieldType.STRING, "프로필 이모지"),
  USER_ID("id", JsonFieldType.NUMBER, "사용자 인덱스"),
  SEQUENCE("sequence", JsonFieldType.NUMBER, "닉네임 사용 순번"),
  NICKNAME("nickname", JsonFieldType.STRING, "사용자 닉네임(최대 20자)");


  private final String path;
  private final JsonFieldType type;
  private final String description;
}
