/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//package com.marekrychlik.Demo;
package io.github.jonathanlink;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.imageio.*;
import java.util.stream.*;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorName;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.apache.pdfbox.rendering.*;

/**
 * This is an example on how to remove all text from PDF document.
 *
 * @author Ben Litchfield
 */
public final class RemoveAllText
{
    /**
     * Default constructor.
     */
    private RemoveAllText()
    {
        // example class should not be instantiated
    }

    /**
     * This will remove all text from a PDF document.
     *
     * @param args The command line arguments.
     *
     * @throws IOException If there is an error parsing the document.
     */
    public static void main(String[] args) throws IOException
    {
	if (args.length != 2) {
	    usage();
	} else {
	    mapFile(args[0], args[1]);
	}
    }


    public static void mapFile(String inputFile, String outputFile) throws IOException
    {
	try (PDDocument document = PDDocument.load(new File(inputFile)) ) {
	    if (document.isEncrypted()) {
		    System.err.println("Error: Encrypted documents are not supported for this example.");
		    System.exit(1);
	    }
	    for (PDPage page : document.getPages())
		{
		    List<Object> newTokens = createTokensWithoutText(page);
		    PDStream newContents = new PDStream(document);
		    writeTokensToStream(newContents, newTokens);
		    page.setContents(newContents);
		    processResources(page.getResources());
		}
	    document.save(outputFile);
	}
    }

    // Solution for the 2.0 version:
    //
    // PDDocument document = PDDocument.load(new File(pdfFilename));
    // PDFRenderer pdfRenderer = new PDFRenderer(document);
    // for (int page = 0; page < document.getNumberOfPages(); ++page)
    // { 
    //     BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

    //     // suffix in filename will be used as the file format
    //     ImageIOUtil.writeImage(bim, pdfFilename + "-" + (page+1) + ".png", 300);
    // }
    // document.close();

    public static String[] mapFileToImages(String inputFile, String outputFile, int[] pages) throws IOException
    {
	try (PDDocument document = PDDocument.load(new File(inputFile)) ) {
	    if (document.isEncrypted()) {
		System.err.println("Error: Encrypted documents are not supported for this example.");
		System.exit(1);
	    }
	    int numPages = document.getNumberOfPages();
//	    if (pages == null) {
//		pages = IntStream.rangeClosed(1, numPages).toArray();
//	    }
	    if (pages == null) {
	        pages = new int[numPages];
	        for (int i = 0; i < numPages; i++) {
	            pages[i] = i + 1;
	        }
	    }
	    numPages = pages.length;
	    System.out.println("Pages: " + Arrays.toString(pages));
	    for (int j=0; j < numPages; ++j) {
		int pagenum = pages[j];
		PDPage page = document.getPages().get(pagenum-1);
		List<Object> newTokens = createTokensWithoutText(page);
		    PDStream newContents = new PDStream(document);
		    writeTokensToStream(newContents, newTokens);
		    page.setContents(newContents);
		    processResources(page.getResources());
		}
	    String[] pageImages = new String[numPages];
	    PDFRenderer pdfRenderer = new PDFRenderer(document);
	    for (int j = 0; j < numPages; ++j) { 
		int pagenum = pages[j];
		BufferedImage bim = pdfRenderer.renderImageWithDPI(pagenum-1, 300, ImageType.RGB);
		// suffix in filename will be used as the file format
		String imgFile = outputFile + "-" + pagenum + ".jpg";
		ImageIO.write(bim, "JPEG", new File(imgFile));
		pageImages[j]=imgFile;
		// pageImages.set(page, get2DPixelArrayFast(bim));
	    }
	    document.close();
	    // document.save(outputFile);
	    return pageImages;
	}
    }
    
    private static void processResources(PDResources resources) throws IOException
    {
        for (COSName name : resources.getXObjectNames())
        {
            PDXObject xobject = resources.getXObject(name);
            if (xobject instanceof PDFormXObject)
            {
                PDFormXObject formXObject = (PDFormXObject) xobject;
                writeTokensToStream(formXObject.getContentStream(),
                        createTokensWithoutText(formXObject));
                processResources(formXObject.getResources());
            }
        }
        for (COSName name : resources.getPatternNames())
        {
            PDAbstractPattern pattern = resources.getPattern(name);
            if (pattern instanceof PDTilingPattern)
            {
                PDTilingPattern tilingPattern = (PDTilingPattern) pattern;
                writeTokensToStream(tilingPattern.getContentStream(),
                        createTokensWithoutText(tilingPattern));
                processResources(tilingPattern.getResources());
            }
        }
    }

    private static void writeTokensToStream(PDStream newContents, List<Object> newTokens) throws IOException
    {
        try (OutputStream out = newContents.createOutputStream(COSName.FLATE_DECODE))
        {
            ContentStreamWriter writer = new ContentStreamWriter(out);
            writer.writeTokens(newTokens);
        }
    }

    private static List<Object> createTokensWithoutText(PDContentStream contentStream) throws IOException
    {
        PDFStreamParser parser = new PDFStreamParser(contentStream);
        Object token = parser.parseNextToken();
        List<Object> newTokens = new ArrayList<>();
        while (token != null)
        {
            if (token instanceof Operator)
            {
                Operator op = (Operator) token;
                String opName = op.getName();
                if (OperatorName.SHOW_TEXT_ADJUSTED.equals(opName)
                        || OperatorName.SHOW_TEXT.equals(opName)
                        || OperatorName.SHOW_TEXT_LINE.equals(opName))
                {
                    // remove the argument to this operator
                    newTokens.remove(newTokens.size() - 1);

                    token = parser.parseNextToken();
                    continue;
                }
                else if (OperatorName.SHOW_TEXT_LINE_AND_SPACE.equals(opName))
                {
                    // remove the 3 arguments to this operator
                    newTokens.remove(newTokens.size() - 1);
                    newTokens.remove(newTokens.size() - 1);
                    newTokens.remove(newTokens.size() - 1);

                    token = parser.parseNextToken();
                    continue;
                }
            }
            newTokens.add(token);
            token = parser.parseNextToken();
        }
        return newTokens;
    }

    private static int[][] get2DPixelArrayFast(BufferedImage image) {
	byte[] pixelData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	int width = image.getWidth();
	int height = image.getHeight();
	boolean hasAlphaChannel = image.getAlphaRaster() != null;

	int[][] result = new int[height][width];
	if (hasAlphaChannel) {
	    int numberOfValues = 4;
	    for (int valueIndex = 0, row = 0, col = 0; valueIndex + numberOfValues - 1 < pixelData.length; valueIndex += numberOfValues) {            
		int argb = 0;
		argb += (((int) pixelData[valueIndex] & 0xff) << 24); // alpha value
		argb += ((int) pixelData[valueIndex + 1] & 0xff); // blue value
		argb += (((int) pixelData[valueIndex + 2] & 0xff) << 8); // green value
		argb += (((int) pixelData[valueIndex + 3] & 0xff) << 16); // red value
		result[row][col] = argb;

		col++;
		if (col == width) {
		    col = 0;
		    row++;
		}
	    }
	} else {
	    int numberOfValues = 3;
	    for (int valueIndex = 0, row = 0, col = 0; valueIndex + numberOfValues - 1 < pixelData.length; valueIndex += numberOfValues) {
		int argb = 0;
		argb += -16777216; // 255 alpha value (fully opaque)
		argb += ((int) pixelData[valueIndex] & 0xff); // blue value
		argb += (((int) pixelData[valueIndex + 1] & 0xff) << 8); // green value
		argb += (((int) pixelData[valueIndex + 2] & 0xff) << 16); // red value
		result[row][col] = argb;

		col++;
		if (col == width) {
		    col = 0;
		    row++;
		}
	    }
	}

	return result;
    }

    /**
     * This will print the usage for this document.
     */
    private static void usage()
    {
        System.err.println(
                "Usage: java " + RemoveAllText.class.getName() + " <input-pdf> <output-pdf>");
    }

}
