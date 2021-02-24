package mortgage;

import java.io.*;


public class Mortgage {
    

    /*
        This functions does not work for negative exponents
    */
    public Double power(Double base, Double exponent) {
        double total = 1;
        for (int i = 0; i < exponent; ++i) {
            total = total*base;
        }
        return total;
    }
    /*
        This function assumes a particular format for the input files
    */
    public Double calcMortgage(String[] parts) {
        Double totalLoan = Double.parseDouble(parts[1]);
        Double interest = Double.parseDouble(parts[2]) / 100; /*Need to divide by 100 otherwise interest is not in percentages*/
        Double years = Double.parseDouble(parts[3]);
        Double montlyInterest = interest / 12;
        Double numOfPayments = years * 12;
        Double mortgage = totalLoan*(((montlyInterest)*power((1 + montlyInterest),(numOfPayments))) / (power((1 + montlyInterest),(numOfPayments))-1));
        return mortgage;
    } 

    public String outputProspect(String line, int prospectNum) {
        String[] parts = line.split(",");
        
        String separator = "****************************************************************************************************\n";
        String individual = "Prospect " + prospectNum + ": " + parts[0] + " wants to borrow ";
        String loan = parts[1] + "€ for a period of " + parts[3] + " years and pay ";
        String mortgage = calcMortgage(parts) + "€ each month\n";
        String result = separator + "\n" + individual + loan + mortgage + separator;
        return result;
    }

}