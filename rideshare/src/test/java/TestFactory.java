import java.util.Date;
import model.CrashFactory;
import model.Name;
import model.ViolationFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class TestFactory {
    @Test
    public void testViolationCreate() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> ViolationFactory.getINSTANCE().create("a", new Date(), null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> ViolationFactory.getINSTANCE().create("a", new Date(), new Name("a", "a")));

        Assertions.assertNotNull(ViolationFactory.getINSTANCE().create("Speeding", new Date(), new Name("a", "a")));
    }

    @Test
    public void testCrashCreate() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> CrashFactory.getINSTANCE().create("a", new Date(), null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> CrashFactory.getINSTANCE().create(null, new Date(), new Name("a", "a")));

        Assertions.assertNotNull(CrashFactory.getINSTANCE().create("fender-bender", new Date(), new Name("a", "a")));
    }
}
