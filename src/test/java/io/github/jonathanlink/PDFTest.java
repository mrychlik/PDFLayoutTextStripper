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

public class PDFTest {
    String tablePDF;

    protected void setUp() {
	tablePDF = "./samples/bus.pdf";
    }

    /**
     * Test parsing bus schedule
     */
    @Test
    public void parseTable() {
	String string = null;
	String filename = tablePDF;
	try {
	    PDFParser pdfParser = new PDFParser(new RandomAccessFile(new File(filename), "r"));
	    pdfParser.parse();
	    PDDocument pdDocument = new PDDocument(pdfParser.getDocument());
	    PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
	    string = pdfTextStripper.getText(pdDocument);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	System.out.println(string);
    }
}
