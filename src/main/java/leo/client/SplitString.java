///////////////////////////////////////////////////////////////////////
//	Name:	SplitString
//	Desc:	The central client object.
//	Date:	3/14/2009 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;


// imports

import java.awt.*;
import java.util.Vector;


public class SplitString {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final FontMetrics fm;
    private String text;
    private Vector<String> result = new Vector<String>();
    private final int width;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public SplitString(String text, int newWidth, Graphics2D g) {
        fm = g.getFontMetrics();
        width = newWidth;

        result.add(text);
        result = split(result);
    }


    /////////////////////////////////////////////////////////////////
    // Split the string
    /////////////////////////////////////////////////////////////////
    private Vector<String> split(Vector<String> results) {
        String remaining = results.lastElement();
        results.remove(remaining);

        // this will be the next line
        String newLine = "";

        // add words to the line until its too full
        boolean continuing = true;
        while (continuing) {
            // get the next word
            String nextWord = getNextWord(remaining);

            // if the next word is too small, we're done
            if (nextWord.length() < 1) {
                results.add(newLine);
                return results;
            }

            // if it fits, modify the strings, and iterate once more
            if (fm.stringWidth(newLine + nextWord) < width) {
                if (newLine.length() < 1) {
                    newLine = newLine + nextWord.trim();
                } else {
                    newLine = newLine + " " + nextWord.trim();
                }
                remaining = removeNextWord(remaining);
            } else {
                continuing = false;
            }
        }

        // if we've gone this far, prepare for recursion
        results.add(newLine);
        results.add(remaining);
        return split(results);
    }


    /////////////////////////////////////////////////////////////////
    // Get the next word
    /////////////////////////////////////////////////////////////////
    private String getNextWord(String line) {
        // return the first word in this line
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ' && i > 0) {
                return line.substring(0, i);
            }
        }
        return line;
    }


    /////////////////////////////////////////////////////////////////
    // Remove the next word
    /////////////////////////////////////////////////////////////////
    private String removeNextWord(String line) {
        // return the first word in this line
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ' && i > 0) {
                return line.substring(i);
            }
        }
        return "";
    }


    /////////////////////////////////////////////////////////////////
    // Get the strings
    /////////////////////////////////////////////////////////////////
    public Vector<String> getStrings() {
        return result;
    }
}
