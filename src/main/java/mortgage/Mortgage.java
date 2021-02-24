package mortgage;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Mortgage {
    /*
     *   This methods does not work for negative exponents, this method was created since it's assumed that Math.power() is not allowed. 
     */
    public Double power(Double base, Double exponent) {
        double total = 1;
        for (int i = 0; i < exponent; ++i) {
            total = total*base;
        }
        return total;
    }

    /* Not satisfied with this method, should be possible to only use Regex to get a correct format
     * This method assumes the name/first field cannot contain a comma with a proceeding digit, 
     * in this case we assume that we've found the next field
     * INVARIANT: Should never be used on empty string or strings which do not contain any digits or commas.
     */

    public String safeFormatName(String line) {
        String name = "";
        int prevIndex = 0; 
        for (int i = 0; i < line.length(); ++i) {
            char currentChar = line.charAt(i);
            if (i > 1) prevIndex = i-1;
            if (Character.isLetter(currentChar)) {
                name = name + currentChar;
            } else if(Character.isDigit(currentChar) && line.charAt(prevIndex) == ',') {
                break;
            } 
        }
        return name;
    }

    /*
     *  This method assumes that a prospect has no digits in its name. Not satisfied with method, 
     *  should be possible to only use Regex to get the correct format
     * INVARIANT: Should never be used on empty string or strings which do not contain any digits or commas.
     */
    public String[] safeFormatStats(String line) {
        int i = 0;
        int totalLength = line.length();
        int prevIndex = 0;
        char prevChar = '\0';
        for(i = 0; i < totalLength; ++i) {
            prevIndex = i-1;
            char currentChar = line.charAt(i);
            if (i > 1) {
                prevChar = line.charAt(prevIndex);
                if (prevChar == ',' && Character.isDigit(currentChar)) {
                    break;
                }
            }
        }
        String stats = line.substring(i,totalLength);
        String[] statParts = stats.split(",");
        return statParts;
    }

    /*
        This method assumes a particular format for the input files
    */
    public Double calcMortgage(String[] stats) {
        Double totalLoan = Double.parseDouble(stats[0]);
        Double interest = Double.parseDouble(stats[1]) / 100; /*Need to divide by 100 otherwise interest is not in percentages*/
        Double years = Double.parseDouble(stats[2]);
        Double montlyInterest = interest / 12;
        Double numOfPayments = years * 12;
        Double mortgage = totalLoan*(((montlyInterest)*power((1 + montlyInterest),(numOfPayments))) / (power((1 + montlyInterest),(numOfPayments))-1));
        return mortgage;
    } 

    /*
     *
     * 
     */
    public String outputProspect(String line, int prospectNum) throws IllegalArgumentException{
        String name = safeFormatName(line);
        String[] stats = safeFormatStats(line);
        if (name.length() == 0 || stats.length == 0) {
           throw new IllegalArgumentException("Method was called with empty string, not a valid argument");
        }
        String separator = "****************************************************************************************************\n";
        String individual = "Prospect " + prospectNum + ": " + name + " wants to borrow ";
        String loan = stats[0] + "€ for a period of " + stats[2] + " years and pay ";
        String mortgage = calcMortgage(stats) + "€ each month\n";
        String result = separator + "\n" + individual + loan + mortgage + separator;
        return result;
    }

    public void outputData(File filename) throws FileNotFoundException {
        Scanner scan = new Scanner(filename);
        int i = 1;
        scan.nextLine(); //Skip over line which explains formatting of the file (it is assumed to always appear and will appear in other test-files)
        while(scan.hasNextLine()) {
            String currentLine = scan.nextLine();
            if (currentLine.length() > 7) { //This assumes that a valid string is eg. a,1,2,3
                System.out.println(outputProspect(currentLine,i));
                ++i;
            }
        }
        scan.close();
    }

}