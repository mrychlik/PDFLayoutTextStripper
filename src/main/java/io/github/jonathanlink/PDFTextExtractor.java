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
	PDDocument pdDocument;
	String string = null;
	RandomAccessFile file;
	try { 
	    file = new RandomAccessFile(new File(filename), "r");
	    PDFParser pdfParser = new PDFParser(file);
	    pdfParser.parse();
	    pdDocument = pdfParser.getPDDocument();
	    PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
	    string = pdfTextStripper.getText(pdDocument);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    file.close();
	    pdDocument.close();
	}

	return string;
    }
}
