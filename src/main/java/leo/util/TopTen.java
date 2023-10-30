///////////////////////////////////////////////////////////////////////
//	Name:	TopTen
//	Desc:	The top ten list generator
//	Date:	8/28/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.util;

import leo.server.DatabaseManager;
import leo.server.Player;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Vector;


public class TopTen {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final String PATH = "./users/";


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static final boolean active = true;
    private static TopTenNode firstNode = null;
    private static final Vector<String> topTen = new Vector<String>();
    private static int count = 0;
    private static int lastRating = -1;

    private static DatabaseManager db;

    /////////////////////////////////////////////////////////////////
    // Main module
    /////////////////////////////////////////////////////////////////
    public static void main() {
        long t1, t2, dt;
        Date time;

        db = new DatabaseManager();

        while (active) {
            try {
                // get the time
                time = new Date();
                t1 = time.getTime();

                // Search
                File files = new File(PATH);

                // Process the list
                process(files.list());

                // Cleanup
                firstNode = null;
                topTen.clear();
                lastRating = -1;
                count = 0;
                System.gc();

                // get the time again
                time = new Date();
                t2 = time.getTime();

                // compare times
                dt = t2 - t1;

                Logger.info("Top 10 calculated, taking " + dt + " milliseconds.");

                Thread.sleep((1000 * 60 * 60));
                //Thread.sleep((1000));

            } catch (Exception e) {
                System.out.println(e);
                //active = false;
            }
        }


    }


    /////////////////////////////////////////////////////////////////
    // Process
    /////////////////////////////////////////////////////////////////
    private static void process(String[] list) {
        try {
            for (int i = 0; i < list.length; i++) {
                Player player = getPlayer(list[i]);
                if (player != null && player.getRating() > 100) {
                    TopTenNode node = new TopTenNode(player);
                    if (firstNode == null)
                        firstNode = node;
                    else
                        firstNode.add(node);
                }
            }

            // Get the sorted data
            search(firstNode);

            // Save it to the file
            save();

        } catch (Exception e) {
            Logger.error(e);
            //active = false;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Save
    /////////////////////////////////////////////////////////////////
    public static void save() {
        try {
            FileWriter writer = new FileWriter("index.html");
            StringBuffer buffer = new StringBuffer();
            buffer.append("<HTML><HEAD><TITLE>Leopold</TITLE>");
            buffer.append("<STYLE TYPE=\"text/css\"> <!-- A { text-decoration: none } //--></STYLE></HEAD>");
            buffer.append("<BODY TEXT=\"000000\" BGCOLOR=\"FFCF0F\" LINK =\"000000\" VLINK=\"000000\" ALINK=\"000000\">");
            buffer.append("<HR SIZE = \"2\" COLOR = \"00000\" WIDTH =\"100%\">");
            buffer.append("<TABLE TEXT=\"000000\" BGCOLOR=\"FFCF0F\" BORDER=\"0\" BORDERCOLOR=\"000000\" CELLPADDING=\"20\" WIDTH=\"100%\">");
            buffer.append("<TD ALIGN=\"center\" VALIGN=\"top\" WIDTH=\"20%\">");
            buffer.append("<TABLE TEXT=\"000000\" BGCOLOR=\"FFFFFF\" BORDER=\"2\" BORDERCOLOR=\"000000\" CELLPADDING=\"8\" CELLSPACING=\"8\">");
            buffer.append("<A HREF=\"./play.html\"><TR><TD><CENTER><A HREF=\"./play.html\">Play Leopold</A></CENTER></TD></TR></A>");
            buffer.append("<A HREF=\"./index.html\"><TR><TD><CENTER><A HREF=\"./index.html\">Main Page</A></CENTER></TD></TR></A>");
            buffer.append("<A HREF=\"./guide.html\"><TR><TD><CENTER><A HREF=\"./guide.html\">Read the Manual</A></CENTER></TD></TR></A>");
            buffer.append("<A HREF=\"http://groups.google.com/group/catluck\"><TR><TD><CENTER><A HREF=\"http://groups.google.com/group/catluck\">Go to the Forum</A></CENTER></TD></TR></A>");
            buffer.append("<A HREF=\"./changes.html\"><TR><TD><CENTER><A HREF=\"./changes.html\">View Changes</A></CENTER></TD></TR></A>");
            buffer.append("<A HREF=\"http://catluck.com\"><TR><TD><CENTER><A HREF=\"http://catluck.com\">Visit catluck.com</A></CENTER></TD></TR></TABLE></TD></A>");
            buffer.append("<TD ALIGN=\"left\" VALIGN=\"top\" WIDTH=\"80%\">");
            //buffer.append("<CENTER><A HREF=\"./index.html\"><IMG SRC=\"leopold.gif\" BORDER=\"2\"></IMG></A></CENTER><BR>");


            buffer.append("<TABLE TEXT=\"000000\" BGCOLOR=\"FFCF0F\" BORDER=\"0\" BORDERCOLOR=\"000000\" CELLPADDING=\"20\" WIDTH=\"100%\">");
            buffer.append("<TD ALIGN=\"left\" VALIGN=\"top\" WIDTH=\"70%\">");
            buffer.append("<CENTER><B><H1>Welcome to Leopold</H1></B></CENTER>");
            buffer.append("<P>Welcome to Leopold!  Leopold 2 is in the works, check back here for updates.</P>");
            buffer.append("<P><CENTER></CENTER></P>");


            buffer.append("</TD><TD ALIGN=\"left\" VALIGN=\"top\" WIDTH=\"30%\">");
            buffer.append("<B>Top 10 Players</B><BR>(generated every hour)<HR SIZE = \"2\" COLOR = \"00000\" WIDTH =\"100%\">");


            for (int i = 0; i < topTen.size(); i++) {
                String string = topTen.elementAt(i);
                buffer.append(string);
            }
            buffer.append("</TD></TABLE></TABLE>");
            buffer.append("<HR SIZE = \"2\" COLOR = \"00000\" WIDTH =\"100%\">");
            buffer.append("</BODY></HTML>");
            String page = buffer.toString();
            writer.write(page, 0, page.length());
            writer.close();

        } catch (Exception e) {
            Logger.error(e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Search
    /////////////////////////////////////////////////////////////////
    private static void search(TopTenNode node) {
        if (node == null) return;

        // Go higher
        search(node.getLeftNode());

        // Spit it out
        if (node.getPlayer().getRating() != lastRating) count++;

        if (count <= 10)
            topTen.add("" + count + ".  " +
                    //(node.getPlayer().getRating() == lastRating ? "(tie) " : "" ) +
                    node.getPlayer().getName() + ": " + node.getPlayer().getRating() + "<BR>");
        else
            return;

        lastRating = node.getPlayer().getRating();

        // Go lower
        search(node.getRightNode());
    }


    /////////////////////////////////////////////////////////////////
    // Load existing player
    /////////////////////////////////////////////////////////////////
    private static Player getPlayer(String name) {
        try {
			/*
			File file = new File("./users/" + name);
			if (!file.exists())
			{	return null;
			}

			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream fois = new ObjectInputStream(fis);
			Player newPlayer = (Player) fois.readObject();
			*/

            Player newPlayer = new Player(db, name);

            // All ok
            return newPlayer;

        } catch (Exception e) {    //active = false;
            return null;
        }
    }

}
