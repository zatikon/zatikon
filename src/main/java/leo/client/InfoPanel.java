///////////////////////////////////////////////////////////////////////
// Name: HiddenUnitStats
// Desc: Display the stats for a hidden unit
// Date: 5/23/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;
//import java.util.Vector;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;

public class InfoPanel extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static final int MARGIN = 5;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;
    private static final int X = ((695 - 185) / 2) - (WIDTH / 2) + 185 ;
    private static final int Y = (Constants.SCREEN_HEIGHT / 2) - (HEIGHT / 2);

    private String msg;
    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public InfoPanel() {
        super(X, Y, WIDTH, HEIGHT);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    /////////////////////////////////////////////////////////////////
    // Draw it
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        // Draw the background image
        Image img = Client.getImages().getImage(Constants.IMG_EDIT_STAT_BOX);
        g.drawImage(img, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

        // Extract table rows and columns using regular expressions
        List<List<String>> tableData = parseHtmlTable(msg);

        // Define table dimensions
        int tableX = getScreenX() + 20; // X position of the table
        int tableY = getScreenY() + MARGIN + 20; // Y position of the table
        int cellPadding = 5; // Space between text and cell border
        int rowHeight = 15; // Height of each row
        int numRows = tableData.size();
        int numCols = tableData.get(0).size();
        int columnWidth = WIDTH / numCols; // Width of each column
        // Set color for text
        g.setColor(Color.WHITE);
        Font font = new Font("Arial", Font.PLAIN, 14);
        g.setFont(font);

        // Draw the table content as text
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                // Draw the cell text
                String text = tableData.get(row).get(col);
                int x = tableX + col * columnWidth;
                int y = tableY + row * rowHeight;
                g.drawString(text, x + cellPadding, y + rowHeight / 2 + 5); // Center the text vertically
            }
        }

        /*
        Vector<String> lines = null;
        lines = new SplitString(msg, getWidth(), g).getStrings();
        int lineCount = lines.size();

        // Optionally, draw a title or message
        g.setColor(Color.WHITE);
        for (int i = 0; i < lines.size(); i++) {
            g.drawString(lines.elementAt(i), getScreenX(), getScreenY() + MARGIN + (i * 15));
        } */
        //g.drawString(msg, getScreenX() - 1, getScreenY() + MARGIN);
    }

    // Helper method to parse the HTML table and extract rows and columns as a list of lists
    private List<List<String>> parseHtmlTable(String html) {
        List<List<String>> tableData = new ArrayList<>();
        // Match <tr>...</tr> including nested content inside
        Pattern rowPattern = Pattern.compile("<tr>(.*?)</tr>", Pattern.DOTALL);
        // Match <td> or <th> content, ignoring any "style" attributes
        Pattern cellPattern = Pattern.compile("<(td|th)(?:\\s+[^>]*style=['\"][^'\"]*['\"][^>]*|[^>]*)>(.*?)</\\1>", Pattern.DOTALL);

        Matcher rowMatcher = rowPattern.matcher(html);
        while (rowMatcher.find()) {
            String rowContent = rowMatcher.group(1);
            List<String> row = new ArrayList<>();

            Matcher cellMatcher = cellPattern.matcher(rowContent);
            while (cellMatcher.find()) {
                row.add(cellMatcher.group(2).trim()); // Get the text inside <td> or <th> without style
            }

            tableData.add(row); // Add the row to the table data
        }

        return tableData;
    }
    /*
    public void draw(Graphics2D g, Frame mainFrame) { //if (unit == null) return;
        Image img = Client.getImages().getImage(Constants.IMG_STAT_PANEL);
        g.drawImage(img, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

        g.setColor(Color.white);
        g.drawString(msg, getScreenX() - 1, getScreenY() + MARGIN);
    }*/

    public boolean clickAt(int x, int y) {
        Client.getGameData().removeInfoPanel();
        return true;
    }
}

