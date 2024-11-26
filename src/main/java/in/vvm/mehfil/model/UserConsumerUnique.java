package in.vvm.mehfil.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import in.vvm.mehfil.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the id value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = UserConsumerUnique.UserConsumerUniqueValidator.class
)
public @interface UserConsumerUnique {

    String message() default "{Exists.user.consumer}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UserConsumerUniqueValidator implements ConstraintValidator<UserConsumerUnique, Long> {

        private final UserService userService;
        private final HttpServletRequest request;

        public UserConsumerUniqueValidator(final UserService userService,
                final HttpServletRequest request) {
            this.userService = userService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(userService.get(Long.parseLong(currentId)).getConsumer())) {
                // value hasn't changed
                return true;
            }
            return !userService.consumerExists(value);
        }

    }

}
