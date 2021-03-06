package com.telstra.webauth.validator;

import com.telstra.webauth.model.Password;
import com.telstra.webauth.model.User;
import com.telstra.webauth.service.PasswordService;
import com.telstra.webauth.service.UserService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ChangePasswordValidator implements Validator {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        User existingUser = userService.findByUsername(user.getUsername());
        
        if (existingUser != null && !bCryptPasswordEncoder.matches(user.getOldPassword(), existingUser.getPassword())) {
            errors.rejectValue("oldPassword", "Invalid.changePasswordForm.password");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
        else if(!validatePassword(user.getPassword())) {
        	errors.rejectValue("password", "Weak.changePasswordForm.password");
        }
        else if(repeatPassword(existingUser.getId(), user.getPassword())) {
        	errors.rejectValue("password", "Repeat.changePasswordForm.password");
        }
    }
    
	private boolean repeatPassword(Long userId, String password) {
		List<Password> passwordHistory = passwordService.retrievePasswordHistory(userId);
		return passwordHistory.stream().anyMatch(p->bCryptPasswordEncoder.matches(password, p.getPassword()));
	}

	// validate the password
	public static boolean validatePassword(final String password) {
		boolean result = false;
		String pattern = buildValidationPattern(true, true, true, false, 1, 20);
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(password);
		result = m.matches();
		return result;
	}

	public static String buildValidationPattern(boolean forceSpecialChar, boolean forceCapitalLetter,
			boolean forceNumber, boolean allowSpace, int minLength, int maxLength) {
		StringBuilder patternBuilder = new StringBuilder("((?=.*[a-z])");

		if (forceSpecialChar) {
			patternBuilder.append("(?=.*[@#$%])");
		}
		if (forceCapitalLetter) {
			patternBuilder.append("(?=.*[A-Z])");
		}
		if (forceNumber) {
			patternBuilder.append("(?=.*[0-9])");
		}
		if (!allowSpace) {
			patternBuilder.append("(?=\\S+$)");
		}
		patternBuilder.append(".{" + minLength + "," + maxLength + "})");
		return patternBuilder.toString();
	}
}
