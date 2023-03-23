package io.github.jonathanlink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

//import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import io.github.jonathanlink.*;
import java.util.*;

public class PDFLayoutTextStripperTest {
    String tablePDF = "samples/bus.pdf";
    String formPDF = "samples/form.pdf";
    String mysteryPDF = "/home/marek/GitProjects/UnosDatastoreImpl/0693_TL4129.pdf";

    /**
     * Parse a bus schedule
     */
    @Test
    public void parseTable() {
	List<String> pages = PDFTextExtractor.parsePDF(tablePDF);
	for(int p=0; p<pages.size(); p++) {
	    System.out.println(pages.get(p));
	}
    }

    /**
     * Parse a tabular form
     */
    @Test
    public void parseForm() {
	List<String> pages = PDFTextExtractor.parsePDF(formPDF);
	for(int p=0; p<pages.size(); p++) {
	    System.out.println(pages.get(p));
	}

    }

    /**
     * Parse a mistery content
     */
    @Test
    public void parseMystery() {
	List<String> pages = PDFTextExtractor.parsePDF(mysteryPDF);
	for(int p=0; p<pages.size(); p++) {
	    System.out.println(pages.get(p));
	}

    }


}
