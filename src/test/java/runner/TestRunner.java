package runner;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import mortgage.Mortgage;
import com.github.javafaker.*;

public class TestRunner {
    

    /**
     * @brief Manually builds the string that the outputData function should produce.
     * @param name The name of the customer.
     * @param loan The loan amount.
     * @param interest The yearly interest rate.
     * @param years The amount of years the loan should be payed off.
     * @param mortgage The montly rate it requires to pay off the loan in the amount of years supplied.
     * @return The string which the outputData function should produce.
     */
    public String buildCorrectString(String name, Double loan, Double interest, int years, Double mortgage) {
        String separator = "****************************************************************************************************\n\n";
        String individual = "Prospect 0: " + name + " wants to borrow ";
        String loanField = loan.toString() + "€ for a period of " + years + " years and pay ";
        String mortgageField = String.format("%.2f",mortgage) + "€ each month\n";
        String result = separator + individual + loanField + mortgageField + separator;
        return result;
    }

    @Test
    public void testMortgageCalcForPerson() {
        for(int i = 0; i < 1000; ++i) {
            Faker fake = new Faker();
            Mortgage mort = new Mortgage();
            String firstName = fake.name().firstName(); 
            String lastName = fake.name().lastName();
            String fullName = firstName + " " + lastName; //The prospect name in this test will always be nicely formatted
            Double loanAmount = fake.number().randomDouble(0, 1000, 10000);
            Double interest = fake.number().randomDouble(2, 1, 10);
            int years = fake.number().numberBetween(1, 30);
            Double expected = loanAmount * ((interest / 1200) / (1 - Math.pow(1+(interest/1200), -(years * 12)))); //Use the built in Math power to see if the calculations where correct
            String person = fullName + "," + loanAmount.toString() + "," + interest.toString() + "," + years;
            assertEquals(expected, mort.calcMortgage(mort.safeFormatValues(person)), 0.000001);
        }
    }

    @Test
    public void testPowerFunc() {
        for(int i = 0; i < 1000; ++i) {
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
    public void testFormatWeirdStringFromInputFile() {
        for(int i = 0; i < 1000; ++i) {
            Mortgage mort = new Mortgage();
            Faker fake = new Faker();
            String firstName = fake.name().firstName();
            String lastName = fake.name().lastName();
            String weirdName = "\"" + firstName + "," + lastName + "\"";
            String actualName = firstName + " " + lastName;
            Double loanAmount = fake.number().randomDouble(0, 1000, 10000);
            Double interest = fake.number().randomDouble(2, 1, 10);
            int years = fake.number().numberBetween(1, 30);
            String personvalues = weirdName + "," + loanAmount.toString() + "," + interest.toString() + "," + years;
            String parsedName = mort.safeFormatName(personvalues);
            try {
                assertEquals(actualName, parsedName);
            } catch (AssertionFailedError err) {
                System.out.println("diff = " + StringUtils.difference(actualName, parsedName));
                System.out.println("Expected: " + actualName + ", actual: " + parsedName);
                fail();
            }
        }
    }
    
    @Test
    public void testGetvaluesForDifferentNames() {
        for (int i = 0; i < 1000; ++i) {
            Mortgage mort = new Mortgage();
            Faker fake = new Faker();
            String fullName = fake.name().firstName() + " " + fake.name().lastName();
            Double loanAmount = fake.number().randomDouble(0, 1000, 10000);
            Double interest = fake.number().randomDouble(3,1,10);
            int years = fake.number().numberBetween(1, 30);
            String person = fullName + "," + loanAmount.toString() + "," + interest.toString() + "," + years; 
            String[] list = {loanAmount.toString(), interest.toString(), Integer.toString(years)};
            String[] valuesList = mort.safeFormatValues(person);
            assertArrayEquals(list, valuesList);
        }
    }

    @Test
    public void testOutputCorrectData() {
        for(int i=0; i < 1000; ++i) {
            Mortgage mort = new Mortgage();
            Faker fake = new Faker();
            String fullName = fake.name().firstName() + " " + fake.name().lastName();
            Double loanAmount = fake.number().randomDouble(0, 1000, 10000);
            Double interest = fake.number().randomDouble(3,1,10);
            int years = fake.number().numberBetween(1, 30);
            String person = fullName + "," + loanAmount.toString() + "," + interest.toString() + "," + years;
            Double mortgage = loanAmount * ((interest / 1200) / (1 - Math.pow(1+(interest/1200), -(years * 12))));
            String expected = buildCorrectString(fullName, loanAmount, interest, years, mortgage);
            String actual = mort.outputProspect(person, 0);
            try {
                assertEquals(actual, expected);
            } catch (AssertionFailedError err) {
                System.out.println("diff = " + StringUtils.difference(actual, expected));
                System.out.println("Expected: " + actual + ", actual: " + expected);
                fail();
            }
        }
    }

    @Test
    public void testMissingNameExceptionThrown() {
        Mortgage mort = new Mortgage();
        Faker fake = new Faker();
        Double loanAmount = fake.number().randomDouble(0, 1000, 10000);
        Double interest = fake.number().randomDouble(2, 1, 10);
        int years = fake.number().numberBetween(1, 30);
        String valuesOnly = loanAmount.toString() + "," + interest.toString() + "," + years;
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            mort.outputProspect(valuesOnly, 0);
        });
        String expected = "Method was called with empty string, not a valid argument";
        String actual = e.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void testMissingValuesExceptionThrown() {
        Mortgage mort = new Mortgage();
        Faker fake = new Faker();
        String name = fake.name().firstName();
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            mort.outputProspect(name, 0);
        });
        String expected = "Method was called with empty string, not a valid argument";
        String actual = e.getMessage();
        assertEquals(expected, actual);
    }
}