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
	try { 
	    RandomAccessFile file = new RandomAccessFile(new File(filename), "r");
	    PDFParser pdfParser = new PDFParser(file);
	    pdfParser.parse();
	    PDDocument pdDocument = pdfParser.getPDDocument();
	    PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
	    String string = pdfTextStripper.getText(pdDocument);
	    pdDocument.close();
	    return string;
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} 
    }
}
