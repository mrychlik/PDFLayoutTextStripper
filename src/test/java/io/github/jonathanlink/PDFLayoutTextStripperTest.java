package io.github.jonathanlink;

import org.junit.jupiter.api.Test;
import io.github.jonathanlink.*;
import java.util.*;

public class PDFLayoutTextStripperTest {
    String tablePDF = "samples/bus.pdf";
    String formPDF = "samples/form.pdf";
    String mysteryPDF = "/home/marek/GitProjects/UNOSDatastoreImpl/0693_TL4129.pdf";

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
	try {
	    List<String> pages = PDFTextExtractor.parsePDF(mysteryPDF);
	    for(int p=0; p<pages.size(); p++) {
		System.out.println(pages.get(p));
	    }
	} 
	catch(StringIndexOutOfBoundsException e) {
	    e.printStackTrace();
	}

    }


}
