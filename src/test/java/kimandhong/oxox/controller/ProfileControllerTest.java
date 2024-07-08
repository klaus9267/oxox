package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractRestDocsTest;
import kimandhong.oxox.common.WithCustomMockUser;
import kimandhong.oxox.dto.profile.ProfileDto;
import kimandhong.oxox.dto.profile.UpdateProfileDto;
import kimandhong.oxox.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest extends AbstractRestDocsTest {
  @MockBean
  ProfileService profileService;

  @Test
  @WithCustomMockUser
  public void updateProfile() throws Exception {
    ProfileDto profileDto = new ProfileDto(1L, "test emoji", "test nickname", 1L);
    UpdateProfileDto updateProfileDto = new UpdateProfileDto(profileDto.nickname(), profileDto.emoji());

    when(profileService.updateProfile(any(UpdateProfileDto.class))).thenReturn(profileDto);

    mockMvc.perform(patch("/api/profiles")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateProfileDto)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(profileDto)))
        .andDo(document("profile-updateProfile",
            resourceDetails().description("프로필 수정").tag("PROFILE API"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("nickname")
                    .type(JsonFieldType.STRING)
                    .description("사용자 닉네임"),
                fieldWithPath("profileEmoji")
                    .type(JsonFieldType.STRING)
                    .description("프로필 이모지")),
            responseFields(
                fieldWithPath("id")
                    .type(JsonFieldType.NUMBER)
                    .description("사용자 인덱스"),
                fieldWithPath("emoji")
                    .type(JsonFieldType.STRING)
                    .description("프로필 이모지"),
                fieldWithPath("nickname")
                    .type(JsonFieldType.STRING)
                    .description("사용자 닉네임"),
                fieldWithPath("sequence")
                    .type(JsonFieldType.NUMBER)
                    .description("닉네임 사용 순번"))
        ));
  }
}