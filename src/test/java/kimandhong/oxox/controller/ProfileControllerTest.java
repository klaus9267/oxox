package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractRestDocsTest;
import kimandhong.oxox.common.FieldDescriptorHelper;
import kimandhong.oxox.common.FieldEnum;
import kimandhong.oxox.dto.profile.ProfileDto;
import kimandhong.oxox.dto.profile.UpdateProfileDto;
import org.junit.jupiter.api.Test;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends AbstractRestDocsTest {
  @Test
  public void updateProfile() throws Exception {
    ProfileDto profileDto = new ProfileDto(1L, "test emoji", "test nickname", 1L);
    UpdateProfileDto updateProfileDto = new UpdateProfileDto(profileDto.nickname(), profileDto.emoji());

    mockMvc.perform(patch("/api/profiles")
            .contentType(APPLICATION_JSON)
            .header("Authorization", token)
            .content(objectMapper.writeValueAsString(updateProfileDto)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(profileDto)))
        .andDo(document("profile-updateProfile",
            resourceDetails().description("프로필 수정").tag("PROFILE API"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                FieldDescriptorHelper.createFields(
                    FieldEnum.NICKNAME,
                    FieldEnum.EMOJI
                )),
            responseFields(
                FieldDescriptorHelper.createFields(
                    FieldEnum.ID,
                    FieldEnum.EMOJI,
                    FieldEnum.NICKNAME,
                    FieldEnum.SEQUENCE
                ))));
  }
}