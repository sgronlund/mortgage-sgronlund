package runner;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import mortgage.Mortgage;
import java.io.FileNotFoundException;
import java.io.File;
import com.github.javafaker.*;

public class TestRunner {
    
    @Test
    public void testMortgageCalc() {
        for(int i = 0; i < 500; ++i) {
            Faker fake = new Faker();
            Mortgage mort = new Mortgage();
            String firstName = fake.name().firstName(); 
            String lastName = fake.name().lastName();
            String fullName = firstName + " " + lastName; //The prospect name in this test will always be nicely formatted
            Double loanAmount = fake.number().randomDouble(0, 1000, 10000);
            Double interest = fake.number().randomDouble(2, 1, 10);
            int years = fake.number().numberBetween(1, 30);
            Double expected = loanAmount * ((interest / 1200) / (1 - Math.pow(1+(interest/1200), -(years * 12))));
            String person = fullName + "," + loanAmount.toString() + "," + interest.toString() + "," + years;
            assertEquals(expected, mort.calcMortgage(mort.safeFormatStats(person)), 0.000001);
        }
    }

    @Test
    public void testPowerFunc() {
        for(int i = 0; i < 500; ++i) {
            Mortgage mort = new Mortgage();
            Faker fake = new Faker();
            Double base = fake.number().randomDouble(1, 1, 100);
            int exponent = fake.number().numberBetween(1, 100);
            Double expected = Math.pow(base, exponent);
            Double actual = mort.power(base, exponent);
            assertEquals(expected, actual, expected/Math.pow(10,6)); //
        }
    }

    @Test
    public void testFormatFullName() {
        for(int i = 0; i < 500; ++i) {
            Mortgage mort = new Mortgage();
            Faker fake = new Faker();
            String firstName = fake.name().firstName();
            String lastName = fake.name().lastName();
            String weirdName = "\"" + firstName + "," + lastName + "\"";
            String actualName = firstName + " " + lastName;
            Double loanAmount = fake.number().randomDouble(0, 1000, 10000);
            Double interest = fake.number().randomDouble(2, 1, 10);
            int years = fake.number().numberBetween(1, 30);
            String personStats = weirdName + "," + loanAmount.toString() + "," + interest.toString() + "," + years;
            String parsedName = mort.safeFormatName(personStats);
            try {
                assertEquals(actualName, parsedName);
            } catch (AssertionFailedError err) {
                System.out.println("diff = " + StringUtils.difference(actualName, parsedName));
                System.out.println("Expected: " + actualName + ", actual: " + parsedName);
            }
        }
    }

    @Test
    public void testSampleFile() throws FileNotFoundException {
        File file = new File("material/prospects.txt");
        Mortgage mort = new Mortgage();
        mort.outputData(file);
    }

}