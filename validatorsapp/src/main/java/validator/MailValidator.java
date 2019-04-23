package validator;

import org.apache.commons.validator.routines.EmailValidator;

class MailValidator extends EmailValidator {
  MailValidator(boolean allowLocal) {
    super(allowLocal);
  }
}
