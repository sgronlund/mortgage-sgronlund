package runner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import mortgage.Mortgage;

public class TestRunner {
    
    @Test
    public void testPower() {
        Mortgage m = new Mortgage();
        System.out.println(m.outputProspect("Juha,1000,5,2", 1));
        assertEquals(1,1);
    }

}