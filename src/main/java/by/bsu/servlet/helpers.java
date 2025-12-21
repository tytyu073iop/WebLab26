package by.bsu.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class helpers {
    public static Date convertToSqlDate(String userInput) throws IllegalArgumentException {
        if (userInput == null || userInput.trim().isEmpty()) {
            throw new IllegalArgumentException("Date input cannot be empty");
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Strict parsing
        
        try {
            java.util.Date utilDate = dateFormat.parse(userInput.trim());
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD format: " + userInput);
        }
    }
}
