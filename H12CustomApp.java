///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           CSV File Data Manipulator
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
///////////////////////////////// REFLECTION ///////////////////////////////////
//
// 1. Describe the problem you wrote the program to solve:
//    This program is designed to read a specific column from a CSV file and to write
//    data to a CSV file. It addresses the need to manipulate and analyze data stored
//    in CSV format, which is a common requirement in data processing tasks.
// 2. Why did you choose the method header for the read file method (e.g., return type,
//    parameters, throws clause)?
//    The readCSVColumn method returns an ArrayList<String> to handle and store the values
//    of the specified column. By accepting the filename and column name as
//    parameters, the method can identify which file and column to access.
// 3. Why did you choose the method header for the write file method (e.g., return type,
//    parameters, throws clause)?
//    The `writeCSVFile` method returns a `boolean` to indicate success or failure of the
//    file write operation. It takes the filename, headers, and rows of data as parameters
//    to create and write the CSV file. It also handles `FileNotFoundException` internally
//    to notify if the file could not be written.
// 4. What are the biggest challenges you encountered:
//    One of the biggest challenges was ensuring that the CSV file reading and writing handled
//    various edge cases, such as missing files or incorrect column names. Additionally,
//    formatting the CSV correctly to ensure it was readable by other programs was another
//    challenge, and also ensuring the file paths were correct. Another challenge was to think
//    of ways to make my program unique.
// 5. What did you learn from this assignment:
//    This assignment reinforced my understanding of file I/O operations in Java, particularly
//    handling CSV files. I also learned the importance of robust error handling and user input
//    validation to make the program more reliable and user-friendly. I learned creative aspects of
//    programming as the project ideas were made mostly by myself.
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
 * This application manipulates CSV files by performing various data
 * operations based on user input. The main features include reading
 * specific columns, multiple columns, or rows from a CSV file,
 * filtering rows by a keyword, and writing data to a new CSV file.
 * Users are prompted to specify filenames, column names, row numbers,
 * and keywords. The program reads from an existing CSV file and outputs
 * the results to a specified file, handling common file I/O errors and
 * providing informative feedback to the user.
 *
 * @author Ajay Shenoy
 */
public class H12CustomApp {

    /**
     * Writes data to a specified text file. The data is appended to the file, if the file
     * does not exist the file will be created.
     *
     * @param filename The name of the file to write to. The file path is relative to the
     *                 location of the project folder, build off the project folder.
     * @param data The data to write to the file.
     */
    public static void writeToFile(String filename, String data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println(data);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to write to file");
        } catch (IOException e) {
            System.out.println("Error: I/O error occurred");
        }
    }

    /**
     * Reads a specific column from a CSV file and returns it as a list of strings.
     *
     * @param filename The name of the file to read from.
     * @param columnName The name of the column to read.
     * @return The contents of the specified column or an error message
     * if the file is not found or the column does not exist.
     */
    public static ArrayList<String> readCSVColumn(String filename, String columnName) {
        ArrayList<String> columnData = new ArrayList<>();
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                String[] headers = scanner.nextLine().split(",");
                int columnIndex = -1;
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].trim().equals(columnName)) {
                        columnIndex = i;
                        break;
                    }
                }
                if (columnIndex == -1) {
                    columnData.add("Error: Column not found");
                    return columnData;
                }
                while (scanner.hasNextLine()) {
                    String[] values = scanner.nextLine().split(",");
                    if (values.length > columnIndex) {
                        columnData.add(values[columnIndex].trim());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            columnData.add("Error: File not found");
        }
        return columnData;
    }

    /**
     * Reads multiple specific columns from a CSV file and returns contents as a
     * list of lists.
     *
     * @param filename The name of the file to read from.
     * @param columnNames The names of the columns to read.
     * @return The contents of the specified columns or an error message
     * if the file is not found or any column does not exist.
     */
    public static ArrayList<ArrayList<String>> readMultipleCSVColumns
    (String filename, List<String> columnNames) {
        ArrayList<ArrayList<String>> columnsData = new ArrayList<>();
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                String[] headers = scanner.nextLine().split(",");
                ArrayList<Integer> columnIndices = new ArrayList<>();
                for (String columnName : columnNames) {
                    int columnIndex = -1;
                    for (int i = 0; i < headers.length; i++) {
                        if (headers[i].trim().equals(columnName)) {
                            columnIndex = i;
                            break;
                        }
                    }
                    if (columnIndex == -1) {
                        columnsData.add(new ArrayList<String>() {{
                            add("Error: Column not found: " + columnName);
                        }});
                    } else {
                        columnIndices.add(columnIndex);
                        // Initialize list for each column
                        columnsData.add(new ArrayList<>());
                    }
                }
                while (scanner.hasNextLine()) {
                    String[] values = scanner.nextLine().split(",");
                    for (int i = 0; i < columnIndices.size(); i++) {
                        int columnIndex = columnIndices.get(i);
                        if (values.length > columnIndex) {
                            columnsData.get(i).add(values[columnIndex].trim());
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            for (int i = 0; i < columnNames.size(); i++) {
                columnsData.add(new ArrayList<String>() {{
                    add("Error: File not found");
                }});
            }
        }
        return columnsData;
    }

    /**
     * Reads specific rows from a CSV file by indexes and returns as string arrays.
     *
     * @param filename The name of the file to read from.
     * @param rowIndexes The indexes of the rows to read.
     * @return The contents of the specified rows or an error message
     * if the file is not found.
     */
    public static ArrayList<String[]> readCSVRows
    (String filename, List<Integer> rowIndexes) {
        ArrayList<String[]> rowsData = new ArrayList<>();
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                String[] headers = scanner.nextLine().split(",");
                int rowCounter = 0;
                while (scanner.hasNextLine()) {
                    if (rowIndexes.contains(rowCounter)) {
                        rowsData.add(scanner.nextLine().split(","));
                    } else {
                        scanner.nextLine(); // Skip the line
                    }
                    rowCounter++;
                }
            }
        } catch (FileNotFoundException e) {
            rowsData.add(new String[]{"Error: File not found"});
        }
        return rowsData;
    }

    /**
     * Reads the entire CSV file and returns as a list of string arrays.
     *
     * @param filename The name of the file to read from.
     * @return The contents of the CSV file or an error message
     * if the file is not found.
     */
    public static ArrayList<String[]> readWholeCSV(String filename) {
        ArrayList<String[]> fileData = new ArrayList<>();
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                fileData.add(scanner.nextLine().split(","));
            }
        } catch (FileNotFoundException e) {
            fileData.add(new String[]{"Error: File not found"});
        }
        return fileData;
    }

    /**
     * Writes data to a CSV file, includes headers and rows.
     *
     * @param filename The name of the file to write to.
     * @param headers The headers of the CSV file.
     * @param rows The rows of data to write.
     * @return true if the file was created, false otherwise.
     */
    public static boolean writeCSVFile(String filename, String[] headers,
                                       ArrayList<String[]> rows) {
        File file = new File(filename);
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String header : headers) {
                writer.print(header + ",");
            }
            writer.println();
            for (String[] row : rows) {
                for (String value : row) {
                    writer.print(value + ",");
                }
                writer.println();
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to write to file");
            return false;
        }
    }

    /**
     * Reads the entire CSV file and returns all rows that contain
     * the specified keyword.
     *
     * @param filename The name of the file to read from.
     * @param keyword The keyword to search for in the rows.
     * @return A list of rows containing the keyword.
     */
    public static ArrayList<String[]> filterRowsByKeyword(String filename, String keyword) {
        ArrayList<String[]> filteredRows = new ArrayList<>();
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(keyword)) {
                    String[] values = line.split(",");
                    filteredRows.add(values);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found");
        }
        return filteredRows;
    }

    /**
     * Main method to demonstrate the functionality of the application.
     * Asks users for input to perform the operations of the CSV files.
     * If there is an error or invalid input, will ask question again or
     * exit the method via return.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Step 1: Enter the filename to create to write the output to
        String outputFilename;
        try {
            System.out.println("Enter the filename to create to write the output to:");
            if (input.hasNextLine()) {
                outputFilename = input.nextLine().trim();
                if (outputFilename.isEmpty()) {
                    System.out.println("Error: Filename cannot be empty. Exiting program.");
                    return;  // Exit the method and thus the program
                }
            } else {
                System.out.println("Error: No input available. Exiting program.");
                return;  // Exit the method and thus the program
            }
        } catch (NoSuchElementException e) {
            System.out.println("Error: Unexpected end of input. Exiting program.");
            return;  // Exit the method and thus the program
        }

        // Step 2: Enter the filename of the data to use
        String readFilename;
        try {
            System.out.println("Enter the filename of the data you want to use:");
            if (input.hasNextLine()) {
                readFilename = input.nextLine().trim();
                if (readFilename.isEmpty()) {
                    System.out.println("Error: Filename cannot be empty. Exiting program.");
                    return;  // Exit the method and thus the program
                }
            } else {
                System.out.println("Error: No input available. Exiting program.");
                return;  // Exit the method and thus the program
            }
        } catch (NoSuchElementException e) {
            System.out.println("Error: Unexpected end of input. Exiting program.");
            return;  // Exit the method and thus the program
        }

        int option = 0;
        boolean validOption = false;

        // Step 3: Ask the user to choose an option and validate input
        while (!validOption) {
            System.out.println("Choose an option:");
            System.out.println("1. Print a specific column");
            System.out.println("2. Print multiple specific columns");
            System.out.println("3. Print multiple specific rows");
            System.out.println("4. Print all rows containing a specific keyword");
            System.out.println("5. Print the whole CSV");

            String inputOption = input.nextLine();
            try {
                option = Integer.parseInt(inputOption);
                if (option >= 1 && option <= 5) {
                    validOption = true;
                } else {
                    System.out.println("Error: Invalid option. Please enter a " +
                            "number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a valid integer.");
            }
        }

        StringBuilder result = new StringBuilder(); // Capture output here

        switch (option) {
            case 1:
                System.out.println("Enter the column name to read:");
                String columnName1 = input.nextLine().trim();
                if (!columnName1.isEmpty()) {
                    ArrayList<String> columnData = readCSVColumn(readFilename, columnName1);
                    StringBuilder result1 = new StringBuilder();
                    for (String data : columnData) {
                        System.out.println(data);
                        result1.append(data).append("\n");
                    }
                    writeToFile(outputFilename, result1.toString()); // Write results to file
                } else {
                    System.out.println("Error: Column name cannot be empty.");
                }
                break;

            case 2:
                System.out.println("Enter column names to read, separated by commas:");
                String[] columnNames = input.nextLine().split(",");
                if (columnNames.length > 0) {
                    StringBuilder result2 = new StringBuilder();
                    for (String columnName : columnNames) {
                        ArrayList<String> columnData2 =
                                readCSVColumn(readFilename, columnName.trim());
                        result2.append("Column: ").append(columnName).append("\n");
                        for (String data : columnData2) {
                            System.out.println(data);
                            result2.append(data).append("\n");
                        }
                    }
                    writeToFile(outputFilename, result2.toString()); // Write results to file
                } else {
                    System.out.println("Error: Column names cannot be empty.");
                }
                break;

            case 3:
                System.out.println("Enter row numbers to print, " +
                        "separated by commas (starting from 1):");
                String[] rowNumbers = input.nextLine().split(",");
                if (rowNumbers.length > 0) {
                    ArrayList<String[]> rows = new ArrayList<>();
                    try (Scanner scanner = new Scanner(new File(readFilename))) {
                        int currentRow = 1;
                        while (scanner.hasNextLine()) {
                            String[] values = scanner.nextLine().split(",");
                            for (String rowNumber : rowNumbers) {
                                if (Integer.parseInt(rowNumber.trim()) == currentRow) {
                                    rows.add(values);
                                }
                            }
                            currentRow++;
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("Error: File not found");
                    }
                    StringBuilder result3 = new StringBuilder();
                    for (String[] row : rows) {
                        for (String value : row) {
                            System.out.print(value + " ");
                            result3.append(value).append(" ");
                        }
                        System.out.println();
                        result3.append("\n");
                    }
                    writeToFile(outputFilename, result3.toString()); // Write results to file
                } else {
                    System.out.println("Error: Row numbers cannot be empty.");
                }
                break;

            case 4:
                System.out.println("Enter the keyword to search for:");
                String keyword = input.nextLine().trim();
                if (!keyword.isEmpty()) {
                    ArrayList<String[]> filteredRows = filterRowsByKeyword(readFilename, keyword);
                    StringBuilder result4 = new StringBuilder();
                    for (String[] row : filteredRows) {
                        for (String value : row) {
                            System.out.print(value + " ");
                            result4.append(value).append(" ");
                        }
                        System.out.println();
                        result4.append("\n");
                    }
                    writeToFile(outputFilename, result4.toString()); // Write results to file
                } else {
                    System.out.println("Error: Keyword cannot be empty.");
                }
                break;

            case 5:
                try (Scanner scanner = new Scanner(new File(readFilename))) {
                    StringBuilder result5 = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        System.out.println(line);
                        result5.append(line).append("\n");
                    }
                    writeToFile(outputFilename, result5.toString()); // Write results to file
                } catch (FileNotFoundException e) {
                    System.out.println("Error: File not found");
                }
                break;

            default:
                System.out.println("Invalid option");
                break;
        }
    }
}
