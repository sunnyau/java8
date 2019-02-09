package optional;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OptionalTest {



    @Test
    public void test() {
        Optional<String> s1 = Optional.ofNullable("hello");
        assertTrue( s1.isPresent() );
        Optional<String> s2 = Optional.ofNullable(null);
        assertFalse( s2.isPresent() );
    }

    @Test
    public void testOrElse() {
        String string = null;
        assertEquals("else", Optional.ofNullable(string).orElse("else"));
    }
}
