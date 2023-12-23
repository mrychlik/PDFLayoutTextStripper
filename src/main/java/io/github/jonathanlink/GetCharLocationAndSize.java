//package com.marekrychlik.Demo;
package io.github.jonathanlink;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
  
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
  
/**
 * Source: <a href="https://www.tutorialkart.com/pdfbox/how-to-extract-coordinates-or-position-of-characters-in-pdf/">TutorialKart</a>
 * This is an example on how to get the x/y coordinates and size of each character in PDF
 */
public class GetCharLocationAndSize extends PDFTextStripper {
  
    public GetCharLocationAndSize() throws IOException {
    }
  
    /**
     * @throws IOException If there is an error parsing the document.
     */
    public static void main(String[] args) throws IOException {
	if (args.length != 1) {
	    usage();
	}
	String fileName = args[0];
	doFile(fileName);
    }
    
    public static void doFile(String fileName) throws IOException {
	PDDocument document = null;
        try {
	    document = PDDocument.load( new File(fileName) ); 
            PDFTextStripper stripper = new GetCharLocationAndSize();
            stripper.setSortByPosition( true );
            stripper.setStartPage( 0 );
            stripper.setEndPage( document.getNumberOfPages() );
  
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);
        } finally {
            if( document != null ) {
                document.close();
            }
        }
    }
  
    /**
     * Override the default functionality of PDFTextStripper.writeString()
     */
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (TextPosition text : textPositions) {
            System.out.println(text.getUnicode()+ " [(X=" + text.getXDirAdj() + ",Y=" +
			       text.getYDirAdj() + ") height=" + text.getHeightDir() + " width=" +
			       text.getWidthDirAdj() + "]");
        }
    }

    /**
     * This will print the usage for this document.
     */
    private static void usage()
    {
        System.err.println("Usage: java " + GetCharLocationAndSize.class.getName() + " <input-pdf>");
    }


}
