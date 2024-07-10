package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractRestDocsTest;
import kimandhong.oxox.common.FieldDescriptorHelper;
import kimandhong.oxox.common.FieldEnum;
import kimandhong.oxox.dto.poll.CreatePollDto;
import org.junit.jupiter.api.Test;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PollControllerTest extends AbstractRestDocsTest {
  @Test
  public void createPoll() throws Exception {
    CreatePollDto createPollDto = new CreatePollDto("test title", "test content", null);

    mockMvc.perform(patch("/api/profiles")
            .contentType(APPLICATION_JSON)
            .header("Authorization", token)
            .content(objectMapper.writeValueAsString(createPollDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.title").value(createPollDto.title()))
        .andExpect(jsonPath("$.content").value(createPollDto.content()))
        .andExpect(jsonPath("$.thumbnailUrl").value(1L))
        .andDo(document("poll-createPoll",
            resourceDetails().description("게시글 생성").tag("POLL API"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                FieldDescriptorHelper.createFields(
                    FieldEnum.POLL_TITLE,
                    FieldEnum.POLL_CONTENT
                )),
            responseFields(
                FieldDescriptorHelper.createFields(
                    FieldEnum.ID,
                    FieldEnum.POLL_TITLE,
                    FieldEnum.POLL_CONTENT,
                    FieldEnum.USER_DTO
                ))));
  }
}