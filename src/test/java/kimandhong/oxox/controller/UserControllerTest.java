package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractRestDocsTest;
import kimandhong.oxox.common.FieldDescriptorHelper;
import kimandhong.oxox.common.FieldEnum;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractRestDocsTest {
  @Autowired
  UserRepository userRepository;

  @Test
  public void join() throws Exception {
    Integer id = userRepository.findAll().size() + 1;
    JoinDto joinDto = new JoinDto("test@email.com", "test password", "test nickname", "test emoji");

    mockMvc.perform(post("/api/users/join")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(joinDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(("$.id")).value((long) id))
        .andExpect(jsonPath(("$.email")).value(joinDto.email()))
        .andExpect(jsonPath(("$.nickname")).value(joinDto.nickname()))
        .andExpect(jsonPath(("$.profileEmoji")).value(joinDto.profileEmoji()))
        .andDo(document("user-join",
            resourceDetails().description("일반 회원가입").tag("USER API"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                FieldDescriptorHelper.createFields(
                    FieldEnum.EMAIL,
                    FieldEnum.PASSWORD,
                    FieldEnum.NICKNAME,
                    FieldEnum.EMOJI
                )),
            responseFields(
                FieldDescriptorHelper.createFields(
                    FieldEnum.ID,
                    FieldEnum.EMAIL,
                    FieldEnum.EMOJI,
                    FieldEnum.NICKNAME,
                    FieldEnum.SEQUENCE
                ))));
  }

  @Test
  public void login() throws Exception {
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

    LoginDto loginDto = new LoginDto(user.getEmail(), "test password");
    mockMvc.perform(post("/api/users/login")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$.id")).value(user.getId()))
        .andExpect(jsonPath(("$.email")).value(user.getEmail()))
        .andExpect(jsonPath(("$.nickname")).value(user.getProfile().getNickname()))
        .andExpect(jsonPath(("$.profileEmoji")).value(user.getProfile().getEmoji()))
        .andDo(document("user-login",
            resourceDetails().description("일반 로그인").tag("USER API"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                FieldDescriptorHelper.createFields(
                    FieldEnum.EMAIL,
                    FieldEnum.PASSWORD
                )),
            responseFields(
                FieldDescriptorHelper.createFields(
                    FieldEnum.ID,
                    FieldEnum.EMAIL,
                    FieldEnum.NICKNAME,
                    FieldEnum.SEQUENCE,
                    FieldEnum.EMOJI
                ))));
  }
}