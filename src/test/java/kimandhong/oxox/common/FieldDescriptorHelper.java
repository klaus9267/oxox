package kimandhong.oxox.common;

import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class FieldDescriptorHelper {
  public static FieldDescriptor createField(final FieldEnum fieldEnum) {
    return fieldWithPath(fieldEnum.getPath())
        .type(fieldEnum.getType())
        .description(fieldEnum.getDescription());
  }

  public static List<FieldDescriptor> createFields(final FieldEnum... fieldEnums) {
    return Arrays.stream(fieldEnums)
        .map(FieldDescriptorHelper::createField)
        .toList();
  }
}
