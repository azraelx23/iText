/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package itext.mike;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

public class ListUsedFonts {
    private static final String SRC = "sample-pdf/input/mike-pdf-ori.pdf";

    /**
     * Creates a Set containing information about the fonts in the src PDF file.
     * @param src the path to a PDF file
     * @throws IOException
     */
    public Set<String> listFonts(String src) throws IOException {
        Set<String> set = new TreeSet<String>();
        PdfReader reader = new PdfReader(src);
        PdfDictionary resources;
        for (int k = 1; k <= reader.getNumberOfPages(); ++k) {
            resources = reader.getPageN(k).getAsDict(PdfName.RESOURCES);
            processResource(set, resources);
        }
        return set;
    }
    
    /**
     * Extracts the font names from page or XObject resources.
     * @param set the set with the font names
     * @param resources the resources dictionary
     */
    public static void processResource(Set<String> set, PdfDictionary resource) {
        if (resource == null)
            return;
        PdfDictionary xobjects = resource.getAsDict(PdfName.XOBJECT);
        if (xobjects != null) {
            for (PdfName key : xobjects.getKeys()) {
                processResource(set, xobjects.getAsDict(key));
            }
        }
        PdfDictionary fonts = resource.getAsDict(PdfName.FONT);
        if (fonts == null)
            return;
        PdfDictionary font;
        for (PdfName key : fonts.getKeys()) {
            font = fonts.getAsDict(key);
            String name = font.getAsName(PdfName.BASEFONT).toString();
            if (name.length() > 8 && name.charAt(7) == '+') {
                name = String.format("%s subset (%s)", name.substring(8), name.substring(1, 7));
            }
            else {
                name = name.substring(1);
                PdfDictionary desc = font.getAsDict(PdfName.FONTDESCRIPTOR);
                if (desc == null)
                    name += " nofontdescriptor";
                else if (desc.get(PdfName.FONTFILE) != null)
                    name += " (Type 1) embedded";
                else if (desc.get(PdfName.FONTFILE2) != null)
                    name += " (TrueType) embedded";
                else if (desc.get(PdfName.FONTFILE3) != null)
                    name += " (" + font.getAsName(PdfName.SUBTYPE).toString().substring(1) + ") embedded";
            }
            set.add(name);
        }
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        Set<String> set = new ListUsedFonts().listFonts(SRC);
        for (String fontname : set)
            System.out.println(fontname);
    }
}