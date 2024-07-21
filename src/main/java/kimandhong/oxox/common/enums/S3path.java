package kimandhong.oxox.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3path {
  THUMBNAIL("thumbnail/"),
  PROFILE("profile/");

  private final String value;
}
