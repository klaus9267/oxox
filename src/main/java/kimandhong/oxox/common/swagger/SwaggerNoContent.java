package kimandhong.oxox.common.swagger;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(responses = {@ApiResponse(responseCode = "204", description = "응답 없음")})
@ResponseStatus(HttpStatus.NO_CONTENT)
public @interface SwaggerNoContent {
    String summary() default "";

    String description() default "";
}
