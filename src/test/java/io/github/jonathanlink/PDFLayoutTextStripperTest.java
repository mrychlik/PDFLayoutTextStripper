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

    /**
     * Parse a bus schedule
     */
    @Test
    public void parseTable() {
	List<String> pages = PDFTextExtractor.parsePDF(tablePDF);
	for(int j=0; j<pages.size(); j++) {
	    System.out.println(pages.get(p));
	}
    }

    /**
     * Parse a tabular form
     */
    @Test
    public void parseForm() {
	List<String> pages = PDFTextExtractor.parsePDF(formPDF);
	for(int j=0; j<pages.size(); j++) {
	    System.out.println(pages.get(p));
	}

    }
}
