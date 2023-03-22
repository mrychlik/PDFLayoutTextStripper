package io.github.jonathanlink;

public class PDFLayoutTextStripperExe {

    /**
     * @param args
     */
    public static void main(String[] args) {
		final String tablePDF = "samples/bus.pdf";
		PDFTextExtractor pdfTextExtractor = new PDFTextExtractor();
		String string = pdfTextExtractor.parsePDF(tablePDF);
		System.out.println(string);
    }
}
