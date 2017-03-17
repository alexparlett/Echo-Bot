import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
public class PatternTest {

    @Test
    public void testRegex() {
        Matcher matcher = Pattern.compile("(Hi|Hello|Howdy|Gday)").matcher("@Echo Hi");
        boolean matches = matcher.find();
        assert matches;
    }

}
