package kimandhong.oxox.dto.user;

public record SocialLoginDto(
    String email,
    String displayName,
    String photoUrl,
    String uid
) {
}
