package io.github.jonathanlink;
import java.util.*;

public class PDFLayoutTextStripperExe {

    /**
     * @param args
     */
    public static void main(String[] args) {
	final String tablePDF = "samples/bus.pdf";
	List<String> pages = PDFTextExtractor.parsePDF(tablePDF);
	for (int p = 0; p < pages.size(); p++) {
	    System.out.println(pages.get(p));
	}
    }
}
