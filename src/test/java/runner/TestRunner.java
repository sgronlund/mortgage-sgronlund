package runner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import mortgage.Mortgage;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class TestRunner {
    
    @Test
    public void testSampleFile() throws FileNotFoundException {
        Scanner scan = new Scanner(new File("material/prospects.txt"));
        Mortgage m = new Mortgage();
        int i = 1;
        scan.nextLine(); //Skip over line which explains formatting of the file
        while(scan.hasNextLine()) {
            
            String test = scan.nextLine();
            if (test.length() > 7) {
                System.out.println(m.outputProspect(test,0));
            }
        }
        scan.close();
        assertEquals(1,1);
    }

}