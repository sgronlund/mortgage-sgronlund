package mortgage;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * This is a class which supplies functions to calculate monthly mortgage payments.
 * 
 * @author Sebastian Grönlund
 * 
 */
public class Mortgage {

    /**
     * Calculates a base to the power of an exponent
     * 
     * @param base The factor which should be multiplied by itself
     * @param exponent The amount of times we multipliy the factor by itself
     * @return The value of the base to the power of the exponent
     */
    public Double power(Double base, int exponent) {
        double total = 1;
        for (int i = 0; i < exponent; ++i) {
            total = total*base;
        }
        return total;
    }

    /** 
     * Takes a string and outputs the name of a customer.
     * 
     * @param line A line within the prospect file.
     * @return The sequence of words which was found within the string.
     * 
     * INVARIANT: Should never be used on empty string.
     */

    public String safeFormatName(String line) {
        String name = line.replaceAll("[^a-öA-Ö']+", " "); 
        // Regex which matches one or more non alphabetic characters, i.e. if the user would have entered "John,,,,,,Johnsson" the replaceAll would return John Johnsson. Doesn't remove apostrophe since some last names may have it.
        name = name.trim(); //Removes trailing and leading whitespaces which may remain after using the replaceAll
        return name;
    }

    /**
     *  Tries to find the loan amount, interest rate and amount of years the customer want to pay back the loan in.
     * 
     *  @param line A line within the prospect file
     *  @return An array of strings of the values which were in the string
     *  
     *  Assumes that the order specified in the prospects file is always true, possible to parse the first string in the text-file and
     *  then determine which part of the string are eg. the loan amount etc.
     */
    public String[] safeFormatValues(String line) {
        String values = line.replaceAll("[^0-9.]+", " ");
        // Regex which matches one ore more non-digit character (and dot for decimal values), works in a similiar way to the one in safeFormatName
        values = values.trim();
        String[] valueParts = values.split(" ");
        return valueParts;
    }

    /** 
     * Calculates the montly mortgage payment based on a array of string supplied by safeFormatValues.
     * 
     * @param values the result of calling safeFormatValues on a string in the prospects file.
     * @return The montly mortgage payment the customer will make.
     * 
     * INVARIANT: Function expects the argument to be the output of safeFormatValues.
     */
    public Double calcMortgage(String[] values) {
        Double totalLoan = Double.parseDouble(values[0]);
        Double interest = Double.parseDouble(values[1]) / 100; /*Need to divide by 100 otherwise the interest rate is not in percentages*/
        int years = Integer.parseInt(values[2]);
        Double montlyInterest = interest / 12;
        int numOfPayments = years * 12;
        Double mortgage = totalLoan*(((montlyInterest)*power((1 + montlyInterest),(numOfPayments))) / (power((1 + montlyInterest),(numOfPayments))-1));
        return mortgage;
    } 

    /**
     * Takes the values from safeFormatValues, safeFormatName and calcMortgage and formats them into a nicer string.
     * 
     * @param line a line within the prospect file
     * @param prospectNum the number of the current customer in the file.
     * @return An exact String is described in the instructions
     * 
     */
    public String outputProspect(String line, int prospectNum) throws IllegalArgumentException{
        String name = safeFormatName(line);
        String[] values = safeFormatValues(line);
        if (name.length() == 0 || values.length < 3) {
           throw new IllegalArgumentException("Method was called with empty string, not a valid argument");
        }
        String separator = "****************************************************************************************************\n\n";
        String individual = "Prospect " + prospectNum + ": " + name + " wants to borrow ";
        String loan = values[0] + "€ for a period of " + values[2] + " years and pay ";
        String mortgage = String.format("%.2f",calcMortgage(values)) + "€ each month\n";
        String result = separator + individual + loan + mortgage + separator;
        return result;
    }

    /**
     * Takes a file, which must be formatted as the prospects file, and outputs the data based on the customer in it
     * 
     * @param filename The file we want to output the data of
     * @throws FileNotFoundException
     * 
     * 
     * The line that explains the formatting of the prospects file is always assumed to be present.
     */
    public void outputData(File filename) throws FileNotFoundException {
        Scanner scan = new Scanner(filename);
        int i = 1;
        scan.nextLine(); 
        //Skip over line which explains formatting of the file, i.e. if other files are used to test this program they also are assumed to include a similar line. As stated in previous documentation one could parse this line and then determine how the next lines should be interpreted.
        while(scan.hasNextLine()) {
            String currentLine = scan.nextLine();
            if (currentLine.length() >= 7) { //This assumes that a valid string is eg. a,1,2,3
                System.out.println(outputProspect(currentLine,i));
                ++i;
            }
        }
        scan.close();
    }


    public static void main(String[] args) throws FileNotFoundException {
        File prospects = new File("material/prospects.txt");
        Mortgage mort = new Mortgage();
        mort.outputData(prospects);
    }

}