package in.vvm.mehfil.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import in.vvm.mehfil.service.PincodeService;
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
 * Validate that the pincode value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = PincodePincodeUnique.PincodePincodeUniqueValidator.class
)
public @interface PincodePincodeUnique {

    String message() default "{Exists.pincode.pincode}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PincodePincodeUniqueValidator implements ConstraintValidator<PincodePincodeUnique, String> {

        private final PincodeService pincodeService;
        private final HttpServletRequest request;

        public PincodePincodeUniqueValidator(final PincodeService pincodeService,
                final HttpServletRequest request) {
            this.pincodeService = pincodeService;
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
            if (currentId != null && value.equalsIgnoreCase(pincodeService.get(Long.parseLong(currentId)).getPincode())) {
                // value hasn't changed
                return true;
            }
            return !pincodeService.pincodeExists(value);
        }

    }

}
