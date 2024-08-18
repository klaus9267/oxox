package kimandhong.oxox.application.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3path {
  THUMBNAIL("thumbnail/"),
  PROFILE("profile/");

  private final String value;
}
