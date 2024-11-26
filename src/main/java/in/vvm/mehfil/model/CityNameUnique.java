package in.vvm.mehfil.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import in.vvm.mehfil.service.CityService;
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
 * Validate that the name value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = CityNameUnique.CityNameUniqueValidator.class
)
public @interface CityNameUnique {

    String message() default "{Exists.city.name}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CityNameUniqueValidator implements ConstraintValidator<CityNameUnique, String> {

        private final CityService cityService;
        private final HttpServletRequest request;

        public CityNameUniqueValidator(final CityService cityService,
                final HttpServletRequest request) {
            this.cityService = cityService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(cityService.get(Long.parseLong(currentId)).getName())) {
                // value hasn't changed
                return true;
            }
            return !cityService.nameExists(value);
        }

    }

}
