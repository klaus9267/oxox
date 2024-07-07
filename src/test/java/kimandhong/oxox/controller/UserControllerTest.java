package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractRestDocsTest;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.dto.user.UserDto;
import kimandhong.oxox.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

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
    UserDto userDto = new UserDto(1L, "test email", "test nickname", 1L);
    when(userService.join(any(JoinDto.class))).thenReturn(userDto);

    JoinDto joinDto = new JoinDto(userDto.email(), "test password", userDto.nickname());
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
                    .description("닉네임 입력 순번")
            )));
  }

  @Test
  public void login() throws Exception {
    UserDto userDto = new UserDto(1L, "test email", "test nickname", 1L);
    when(userService.login(any(LoginDto.class))).thenReturn(userDto);

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
                    .description("닉네임 입력 순번")
            )));
  }
}