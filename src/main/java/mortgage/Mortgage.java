package mortgage;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Mortgage {

    /**
     * @brief Calculates a base to the power of an exponent
     * @param base The factor which should be multiplied by itself
     * @param exponent The amoun of times we multipliy the factor by itself
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
     * @brief Takes a string and outputs the name of a customer.
     * @param line A line within the prospect file.
     * @return The sequence of words which was found within the string.
     * INVARIANT: Should never be used on empty string.
     */

    public String safeFormatName(String line) {
        String name = line.replaceAll("[^a-öA-Ö']+", " "); 
        // Regex which matches one or more non alphabetic characters, i.e. if the user would have entered "John,,,,,,Johnsson" the replaceAll would return John Johnsson.
        name = name.trim(); //Removes trailing and leading whitespaces which may remain after using the replaceAll
        return name;
    }

    /**
     *  @brief Tries to find the loan amount, interest rate and amount of years the customer want to pay back the loan in.
     *  @param line A line within the prospect file
     *  @return An array of strings of the values which were in the string
     *  
     *  Function is composed such that if we find a digit and a comma it is assumed that we've found the second part of the input,
     *  i.e. the loan amount.
     */
    public String[] safeFormatValues(String line) {
        String values = line.replaceAll("[^0-9.]+", " ");
        // Regex which matches one ore more non-digit character (and zero for decimal values), works in a similiar way to the one in safeFormatName
        values = values.trim();
        String[] valueParts = values.split(" ");
        return valueParts;
    }

    /** 
     * @brief Calculates the montly mortgage payment based on a array of string supplied by safeFormatValues.
     * @param values the result of calling safeFormatValues on a string in the prospects file.
     * @return The montly mortgage payment the customer will make.
     * 
     * INVARIANT: Function expects the argument to be the output of safeFormatValues.
     */
    public Double calcMortgage(String[] values) {
        Double totalLoan = Double.parseDouble(values[0]);
        Double interest = Double.parseDouble(values[1]) / 100; /*Need to divide by 100 otherwise interest is not in percentages*/
        int years = Integer.parseInt(values[2]);
        Double montlyInterest = interest / 12;
        int numOfPayments = years * 12;
        Double mortgage = totalLoan*(((montlyInterest)*power((1 + montlyInterest),(numOfPayments))) / (power((1 + montlyInterest),(numOfPayments))-1));
        return mortgage;
    } 

    /**
     * @brief Takes the values from safeFormatValues, safeFormatName and calcMortgage and formats them into a nicer string.
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
        String separator = "****************************************************************************************************\n";
        String individual = "Prospect " + prospectNum + ": " + name + " wants to borrow ";
        String loan = values[0] + "€ for a period of " + values[2] + " years and pay ";
        String mortgage = String.format("%.2f",calcMortgage(values)) + "€ each month\n";
        String result = separator + "\n" + individual + loan + mortgage + separator;
        return result;
    }

    /**
     * @brief Takes a file, which must be formatted as the prospects file, and outputs the data based on the people in it
     * @param filename The file we want to output the data of
     * @throws FileNotFoundException
     * 
     * The line that explains the formatting of the prospects file is always assumed to be there.
     */
    public void outputData(File filename) throws FileNotFoundException {
        Scanner scan = new Scanner(filename);
        int i = 1;
        scan.nextLine(); //Skip over line which explains formatting of the file, i.e. if other files are used to test this program they also are assumed to include a similar line
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