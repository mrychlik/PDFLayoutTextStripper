package io.github.jonathanlink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

class PDFTextExtractor {
    String parsePDF(String filename) {
	String string = null;
	try { 
	    RandomAccessFile file = new RandomAccessFile(new File(filename), "r");
	    try {
		PDFParser pdfParser = new PDFParser(file);
		pdfParser.parse();
		PDDocument pdDocument = pdfParser.getPDDocument();
		PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
		string = pdfTextStripper.getText(pdDocument);
		pdDocument.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

	return string;
    }
}
