package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractRestDocsTest;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.dto.user.UserDto;
import kimandhong.oxox.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest extends AbstractRestDocsTest {
  @MockBean
  UserService userService;

  @Test
  public void join() throws Exception {
    JoinDto joinDto = new JoinDto("test@email.com", "test password", "test nickname", "test emoji");
    User user = User.from(joinDto, joinDto.password(), 1);
    ReflectionTestUtils.setField(user, "id", 1L);
    UserDto userDto = UserDto.from(user);

    when(userService.join(any(JoinDto.class))).thenReturn(user);

    mockMvc.perform(post("/api/users/join")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(joinDto)))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
        .andDo(document("user-join",
            resourceDetails().description("일반 회원가입"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email")
                    .type(JsonFieldType.STRING)
                    .description("사용자 이메일"),
                fieldWithPath("nickname")
                    .type(JsonFieldType.STRING)
                    .description("사용자 닉네임"),
                fieldWithPath("password")
                    .type(JsonFieldType.STRING)
                    .description("사용자 패스워드"),
                fieldWithPath("profileEmoji")
                    .type(JsonFieldType.STRING)
                    .description("프로필 이모지")),
            responseFields(
                fieldWithPath("id")
                    .type(JsonFieldType.NUMBER)
                    .description("사용자 고유 번호"),
                fieldWithPath("email")
                    .type(JsonFieldType.STRING)
                    .description("사용자 이메일"),
                fieldWithPath("nickname")
                    .type(JsonFieldType.STRING)
                    .description("사용자 닉네임"),
                fieldWithPath("sequence")
                    .type(JsonFieldType.NUMBER)
                    .description("닉네임 입력 순번"),
                fieldWithPath("profileEmoji")
                    .type(JsonFieldType.STRING)
                    .description("프로필 이모지")
            )));
  }

  @Test
  public void login() throws Exception {
    JoinDto joinDto = new JoinDto("test@email.com", "test password", "test nickname", "test emoji");
    User user = User.from(joinDto, joinDto.password(), 1);
    ReflectionTestUtils.setField(user, "id", 1L);
    UserDto userDto = UserDto.from(user);

    when(userService.login(any(LoginDto.class))).thenReturn(user);

    LoginDto loginDto = new LoginDto(userDto.email(), "test password");
    mockMvc.perform(post("/api/users/login")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginDto)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
        .andDo(document("user-login",
            resourceDetails().description("일반 로그인"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email")
                    .type(JsonFieldType.STRING)
                    .description("사용자 이메일"),
                fieldWithPath("password")
                    .type(JsonFieldType.STRING)
                    .description("사용자 패스워드")),
            responseFields(
                fieldWithPath("id")
                    .type(JsonFieldType.NUMBER)
                    .description("사용자 고유 번호"),
                fieldWithPath("email")
                    .type(JsonFieldType.STRING)
                    .description("사용자 이메일"),
                fieldWithPath("nickname")
                    .type(JsonFieldType.STRING)
                    .description("사용자 닉네임"),
                fieldWithPath("sequence")
                    .type(JsonFieldType.NUMBER)
                    .description("닉네임 입력 순번"),
                fieldWithPath("profileEmoji")
                    .type(JsonFieldType.STRING)
                    .description("프로필 이모지")
            )));
  }
}