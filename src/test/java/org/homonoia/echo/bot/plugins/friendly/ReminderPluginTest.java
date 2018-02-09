package org.homonoia.echo.bot.plugins.friendly;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.MatchResult;
import java.util.stream.IntStream;

/**
 * ReminderPlugin Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Nov 11, 2017</pre>
 */
public class ReminderPluginTest {

    @Before
    public void before() throws Exception {
        IntStream.rangeClosed(3,5)
                .filter(val -> val % 2 == 1)
                .toArray();
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: handleRemindMe(RoomMessage event)
     */
    @Test
    public void testHandleRemindMe() throws Exception {

        MatchResult matchResult = ReminderPlugin.REMIND_ME_PATTERN.matcher("@Echo remind me to do the build at 3pm").toMatchResult();

        matchResult.groupCount();
    }
}
