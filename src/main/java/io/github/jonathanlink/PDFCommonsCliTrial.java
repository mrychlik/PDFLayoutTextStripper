package io.github.jonathanlink;

import org.apache.commons.cli.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.util.List;
import java.io.File;
import java.io.FileWriter;

public class PDFCommonsCliTrial {
    public static void main(String[] args) {
        Options options = new Options();

        // Add option "-h"
        options.addOption("h", "help", false, "Print help");

        // Add option "-R" for removing all text
        Option removeAllTextOption = new Option("R", "remove-all-text", true, "Remove all text");
        removeAllTextOption.setArgs(2); // Set the number of arguments
        removeAllTextOption.setValueSeparator('='); // Set the value separator
        options.addOption(removeAllTextOption);

        // Add option "-C" for getting character location and size
        Option getCharLocationAndSizeOption = new Option("C", "get-char-location-and-size", true, "Get character location and size");
        getCharLocationAndSizeOption.setArgs(1); // Set the number of arguments
        getCharLocationAndSizeOption.setValueSeparator('='); // Set the value separator
        options.addOption(getCharLocationAndSizeOption);

        // Add option "-W" for getting word location and size
        Option getWordLocationAndSizeOption = new Option("W", "get-word-location-and-size", true, "Get word location and size");
        getWordLocationAndSizeOption.setArgs(1); // Set the number of arguments
        getWordLocationAndSizeOption.setValueSeparator('='); // Set the value separator
        options.addOption(getWordLocationAndSizeOption);

        // Add option "-V" for getting words
        Option getWordsOption = new Option("V", "get-words", true, "Get words");
        getWordsOption.setArgs(1); // Set the number of arguments
        getWordsOption.setValueSeparator('='); // Set the value separator
        options.addOption(getWordsOption);

        // Add option "-D" for setting properties
        Option propertyOption = new Option("D", "property", true, "Use value for given properties");
        propertyOption.setArgs(2); // Set the number of arguments
        propertyOption.setValueSeparator('='); // Set the value separator
        options.addOption(propertyOption);
        
     // Add option "-P" for PDFLayoutTextStripper
        Option packageOption = new Option("P", "PDFLayoutTextStripper", true, "Specify a package");
        packageOption.setArgs(2); // Set the number of arguments
        packageOption.setValueSeparator('='); // Set the value separator
        options.addOption(packageOption);
        
        

        // Create a parser
        CommandLineParser parser = new DefaultParser();

        try {
            // Parse the options passed as command line arguments
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                usage(options);
            } else if (cmd.hasOption("R")) {
                String[] loc_args = cmd.getOptionValues("R");
                // Process each pair of input and output file paths
                for (int i = 0; i < loc_args.length; i += 2) {
                    String inputFile = loc_args[i];
                    String outputFile = loc_args[i + 1];
                    RemoveAllText.mapFile(inputFile, outputFile);
                }
            } else if (cmd.hasOption("C")) {
                String[] fileNames = cmd.getOptionValues("C");
                // Process each PDF file specified
                for (String fileName : fileNames) {
                    GetCharLocationAndSize.doFile(fileName);
                }
            } else if (cmd.hasOption("W")) {
                String[] fileNames = cmd.getOptionValues("W");
                // Process each PDF file specified
                for (String fileName : fileNames) {
                    GetWordLocationAndSize.doFile(fileName);
                }
            } else if (cmd.hasOption("V")) {
                String[] fileNames = cmd.getOptionValues("V");
                // Process each PDF file specified
                for (String fileName : fileNames) {
                    GetWords.doFile(fileName);
                }
                
            }else if (cmd.hasOption("P")) {
                String[] loc_args = cmd.getOptionValues("P");
                if (loc_args.length == 2) {
                    doPDFLayoutTextStripper(loc_args[0], loc_args[1]);
                } else {
                    System.err.println("Invalid arguments for -P option");
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private static void doPDFLayoutTextStripper(String fileName, String outputFileName) {
        try (PDDocument document = PDDocument.load(new File(fileName))) {
            PDFLayoutTextStripper stripper = new PDFLayoutTextStripper();
            String text = stripper.getText(document);

            try (FileWriter writer = new FileWriter(outputFileName)) {
                writer.write(text);
                System.out.println("Output written to " + outputFileName);
            }
        } catch (IOException e) {
            System.err.println("Error processing file " + fileName + ": " + e.getMessage());
        }
    }

    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("PDFCommonsCliTrial", options);
    }
}

