package code.guanajuato.gob.mx.activatecode.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by uriel on 3/05/16.
 */
public class ValidEmail {

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //valida correo
    public static boolean isValidEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
