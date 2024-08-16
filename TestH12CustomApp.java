///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           CSV File Data Manipulator Test
// Course:          CS 200, Summer 2024
//
// Author:          Ajay Shenoy
// Email:           ashenoy3@wisc.edu
// Lecturer's Name: Jim Williams
//
///////////////////////////////// CITATIONS ////////////////////////////////////
//
// No citations used.
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * This is the test bench that contains testing methods for the H12CustomApp class.
 * The createTestDataFile and readTestDataFile are private testing methods intended to
 * be used within the test cases.
 *
 * All the test cases within the testH12CustomApp method should be changed to test the
 * methods in your H12CustomApp class.
 *
 * @author Jim Williams
 * @author Ajay Shenoy
 */
public class TestH12CustomApp {

    /**
     * This method runs the selected tests.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        testH12CustomApp();
    }

    /**
     * This is a testing method to create a file with the specified name and fileContents
     * to be used by other testing methods. On a FileNotFoundException a stack trace is printed and
     * then returns.
     *
     * @param testDataFilename The filename of the testing file to create.
     * @param fileContents The data to put into the file.
     */
    private static void createTestDataFile(String testDataFilename, String fileContents) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(testDataFilename);
            writer.print(fileContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    /**
     * This is a testing method to read and return the entire contents of
     * the specified file to be used soley by other testing methods.
     * On a FileNotFoundException a stack trace is printed and then "" returned.
     *
     * @param dataFilename The name of the file to read.
     * @return The contents of the file or "" on error.
     */
    private static String readTestDataFile(String dataFilename) {
        File file = new File(dataFilename);
        Scanner input = null;
        StringBuilder contents = new StringBuilder();
        try {
            input = new Scanner(file);
            while (input.hasNextLine()) {
                contents.append(input.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (input != null) input.close();
        }
        return contents.toString();
    }

    /**
     * Tests that the H12CustomApp read input and write output methods handle
     * the cases described in their method header comments.
     *
     * @return true for passing all testcases, false otherwise
     */
    public static boolean testH12CustomApp() {
        boolean error = false;

        /**
         * Tests the `readCSVColumn` method of the `H12CustomApp` class by reading
         * a specific column ("Age") from a CSV file. It verifies if the method correctly
         * extracts the column values. Expected results are the age values "30", "25", and "35".
         *
         * @return true if the extracted values match the expected results, false otherwise
         */
        // Test case 1: Read specific column from CSV file
        {
            String fileToRead = "testRead.csv";
            String fileContents = "Name,Age,City\nAlice,30,New York\nBob,25," +
                    "Los Angeles\nCharlie,35,Chicago\n";
            createTestDataFile(fileToRead, fileContents);

            ArrayList<String> expectedContents = new ArrayList<>();
            expectedContents.add("30");
            expectedContents.add("25");
            expectedContents.add("35");
            ArrayList<String> actualContents =
                    H12CustomApp.readCSVColumn(fileToRead, "Age");

            if (!actualContents.equals(expectedContents)) {
                error = true;
                System.out.println("readCSVColumn 1) expected: " + expectedContents +
                        " actual: " + actualContents);
            } else {
                System.out.println("readCSVColumn 1) success");
                new File(fileToRead).delete();
            }
        }

        /**
         * Tests the readCSVColumn method of H12CustomApp class for non-existent columns.
         * It checks if the method handles the case where the column does not exist.
         * Expected result is an error message indicating the column was not found.
         *
         * @return true if the test passes, false otherwise
         */
        // Test case 2: Read non-existent column from CSV file
        {
            String fileToRead = "testRead.csv";
            String fileContents = "Name,Age,City\nAlice,30,New York\nBob,25," +
                    "Los Angeles\nCharlie,35,Chicago\n";
            createTestDataFile(fileToRead, fileContents);

            ArrayList<String> expectedContents = new ArrayList<>();
            expectedContents.add("Error: Column not found");
            ArrayList<String> actualContents =
                    H12CustomApp.readCSVColumn(fileToRead, "Salary");

            if (!actualContents.equals(expectedContents)) {
                error = true;
                System.out.println("readCSVColumn 2) expected: " + expectedContents +
                        " actual: " + actualContents);
            } else {
                System.out.println("readCSVColumn 2) success");
                new File(fileToRead).delete();
            }
        }

        /**
         * Tests the writeCSVFile method of H12CustomApp class.
         * It checks if the method correctly writes data to a CSV file.
         * Expected result is a file containing the correct header and data rows.
         *
         * @return true if the test passes, false otherwise
         */
        // Test case 3: Write data to CSV file
        {
            String fileToWrite = "testWrite.csv";
            String[] headers = {"Name", "Age", "City"};
            ArrayList<String[]> rows = new ArrayList<>();
            rows.add(new String[]{"Alice", "30", "New York"});
            rows.add(new String[]{"Bob", "25", "Los Angeles"});
            rows.add(new String[]{"Charlie", "35", "Chicago"});

            boolean writeResult = H12CustomApp.writeCSVFile(fileToWrite, headers, rows);

            String expectedContents = "Name,Age,City,\nAlice,30,New York,\nBob,25,Los Angeles," +
                    "\nCharlie,35,Chicago,\n";
            String actualContents = readTestDataFile(fileToWrite);

            if (!writeResult || !actualContents.equals(expectedContents)) {
                error = true;
                System.out.println("writeCSVFile 3) expected: " + expectedContents +
                        " actual: " + actualContents);
            } else {
                System.out.println("writeCSVFile 3) success");
                new File(fileToWrite).delete();
            }
        }

        /**
         * Tests the writeCSVFile method of H12CustomApp class for handling
         * writing to a non-existent directory.
         * It checks if the method handles the case where the directory does not exist.
         * Expected result is a failure to write to the file, returning false.
         *
         * @return true if the test passes, false otherwise
         */
        // Test case 4: Handle writing to non-existent directory
        {
            String fileToWrite = "nonExistentDir/testWrite.csv";
            String[] headers = {"Name", "Age", "City"};
            ArrayList<String[]> rows = new ArrayList<>();
            rows.add(new String[]{"Alice", "30", "New York"});
            rows.add(new String[]{"Bob", "25", "Los Angeles"});
            rows.add(new String[]{"Charlie", "35", "Chicago"});

            boolean expectedResult = false;
            boolean actualResult = H12CustomApp.writeCSVFile(fileToWrite, headers, rows);

            if (actualResult != expectedResult) {
                error = true;
                System.out.println("writeCSVFile 4) expected: " + expectedResult + " actual: " +
                        actualResult);
            } else {
                System.out.println("writeCSVFile 4) success");
            }
        }

        /**
         * Tests the readWholeCSV method of H12CustomApp class.
         * It checks if the method correctly reads and returns all rows from a CSV file.
         * Expected result is the entire content of the CSV file read into a list of string arrays.
         *
         * @return true if the test passes, false otherwise
         */
        // Test case 5: Read entire CSV file
        {
            String fileToRead = "testReadWhole.csv";
            String fileContents = "Name,Age,City\nAlice,30,New York\nBob,25," +
                    "Los Angeles\nCharlie,35,Chicago\n";
            createTestDataFile(fileToRead, fileContents);

            ArrayList<String[]> expectedContents = new ArrayList<>();
            expectedContents.add(new String[]{"Name", "Age", "City"});
            expectedContents.add(new String[]{"Alice", "30", "New York"});
            expectedContents.add(new String[]{"Bob", "25", "Los Angeles"});
            expectedContents.add(new String[]{"Charlie", "35", "Chicago"});
            ArrayList<String[]> actualContents = H12CustomApp.readWholeCSV(fileToRead);

            if (!actualContents.equals(expectedContents)) {
                error = true;
                System.out.println("readWholeCSV 5) expected: " + expectedContents +
                        " actual: " + actualContents);
            } else {
                System.out.println("readWholeCSV 5) success");
                new File(fileToRead).delete();
            }
        }

        if (error) {
            System.out.println("testH12CustomApp failed");
        } else {
            System.out.println("testH12CustomApp passed");
        }

        return error;
    }
}
