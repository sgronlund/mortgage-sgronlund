package runner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import mortgage.Mortgage;
import java.io.FileNotFoundException;
import java.io.File;
import com.github.javafaker.*;

public class TestRunner {
    
    @Test
    public void testSampleFile() throws FileNotFoundException {
        File file = new File("material/prospects.txt");
        Mortgage mort = new Mortgage();
        mort.outputData(file);
    }

    @Test
    public void testSinglePerson() {
        for(int i = 0; i < 500; ++i) {
            Faker fake = new Faker();
            Mortgage mort = new Mortgage();
            String firstName = fake.name().firstName();
            String lastName = fake.name().lastName();
            String fullName = firstName + " " + lastName;
            Double loanAmount = fake.number().randomDouble(0, 1000, 10000);
            Double interest = fake.number().randomDouble(2, 1, 10);
            Double years = fake.number().randomDouble(0,1,30);
            Double expected = loanAmount * ((interest / 1200) / (1 - Math.pow(1+(interest/1200), -(years * 12))));
            String person = fullName + "," + loanAmount.toString() + "," + interest.toString() + "," + years.toString();
            System.out.println(mort.outputProspect(person, i+1));
            assertEquals(expected, mort.calcMortgage(mort.safeFormatStats(person)), 0.000001);
            
        }
    }

}