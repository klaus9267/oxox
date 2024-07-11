package kimandhong.oxox.common.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(responses = {@ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
public @interface SwaggerOK {
    String summary() default "";

    String description() default "";
}
