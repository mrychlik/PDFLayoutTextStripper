package io.github.jonathanlink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.util.*;

class PDFTextExtractor {
    public static List<String> parsePDF(String filename) {
	ArrayList<String> pages = new ArrayList<String>();
	try { 
	    RandomAccessFile file = new RandomAccessFile(new File(filename), "r");
	    PDFParser pdfParser = new PDFParser(file);
	    pdfParser.parse();
	    PDDocument pdDocument = pdfParser.getPDDocument();
	    PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
	    // Do one page at a time
	    for (int p = 1; p < pdDocument.getNumberOfPages(); p++) {
		pdfTextStripper.setStartPage(p);
		pdfTextStripper.setEndPage(p + 1);
		String pageOfText = pdfTextStripper.getText(pdDocument);
		pages.add(pageOfText);
	    }
	    pdDocument.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return pages;
    }
}
