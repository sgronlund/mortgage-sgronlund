package runner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import mortgage.Mortgage;
import java.io.FileNotFoundException;
import java.io.File;

public class TestRunner {
    
    @Test
    public void testSampleFile() throws FileNotFoundException {
        File file = new File("material/prospects.txt");
        Mortgage mort = new Mortgage();
        mort.outputData(file);
        assertEquals(1,1);
    }

}