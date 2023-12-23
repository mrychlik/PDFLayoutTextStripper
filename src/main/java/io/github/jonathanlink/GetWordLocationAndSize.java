//package com.marekrychlik.Demo;
package io.github.jonathanlink;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.pdmodel.PDDocument;
  
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class GetWordLocationAndSize extends PDFTextStripper {
    public GetWordLocationAndSize() throws IOException {
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        String wordSeparator = getWordSeparator();
        List<TextPosition> word = new ArrayList<>();
        for (TextPosition text : textPositions) {
            String thisChar = text.getUnicode();
            if (thisChar != null) {
                if (thisChar.length() >= 1) {
                    if (!thisChar.equals(wordSeparator)) {
                        word.add(text);
                    } else if (!word.isEmpty()) {
                        printWord(word);
                        word.clear();
                    }
                }
            }
        }
        if (!word.isEmpty()) {
            printWord(word);
            word.clear();
        }
    }

    void printWord(List<TextPosition> word) {
        Rectangle2D boundingBox = null;
        StringBuilder builder = new StringBuilder();
        for (TextPosition text : word) {
            Rectangle2D box = new Rectangle2D.Float(text.getXDirAdj(), text.getYDirAdj(), text.getWidthDirAdj(), text.getHeightDir());
            if (boundingBox == null)
                boundingBox = box;
            else
                boundingBox.add(box);
            builder.append(text.getUnicode());
        }
        System.out.println(builder.toString() + " [(X=" + boundingBox.getX() + ",Y=" + boundingBox.getY()
			   + ") height=" + boundingBox.getHeight() + " width=" + boundingBox.getWidth() + "]");
    }

    public static void main(String args[]) {
	if (args.length != 1) {
	    usage();
	}
	String fileName = args[0];
	doFile(fileName);
    }

    public static void doFile(String fileName) {
	try (PDDocument document = PDDocument.load(new File(fileName))) {
	    PDFTextStripper stripper = new GetWordLocationAndSize();
	    stripper.setSortByPosition(true);
	    stripper.setStartPage(0);
	    stripper.setEndPage(document.getNumberOfPages());

	    Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
	    stripper.writeText(document, dummy);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * This will print the usage for this document.
     */
    private static void usage()
    {
	System.err.println("Usage: java " + GetWordLocationAndSize.class.getName() + " <input-pdf>");
    }

}
