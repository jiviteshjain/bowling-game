/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class highscorefile {
    public int max_value;
    private static String HIGHSCORE_DAT = "HIGHSCORE.DAT";

    private static void _addScore(final String nick, final Date date, final int score)
            throws IOException, FileNotFoundException {
        final String s = String.valueOf(score);
        final String d = formatDate(date);
        final String data = nick + "\t" + d + "\t" + s + "\n";
        String ss;
        String val;
        ss=getScores(nick);
        if(ss.isEmpty()){
            System.out.println("waste");

            final RandomAccessFile out = new RandomAccessFile(HIGHSCORE_DAT, "rw");
        out.skipBytes((int) out.length());
        out.writeBytes(data);
        out.close();}
        else{
            final String[] scoredata = ss.split("\t");
            int max_val;
            max_val=Integer.parseInt(scoredata[2].trim());
            System.out.println("high");
            System.out.println(score);
            System.out.println(max_val);

            if(score>max_val) {
                val = getvalues();
                val = val.replaceAll(ss, data);
                final RandomAccessFile out = new RandomAccessFile(HIGHSCORE_DAT, "rw");
                out.writeBytes(val);
                out.close();
            }
        }
    }
    public static void checkfile(){
        try {
            File myObj = new File("HIGHSCORE.DAT");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void addScore(final String nick, final Date date, final int score) {
        try{
            checkfile();
            _addScore(nick, date, score);
        } catch (final Exception e) {
            System.err.println("Exception in addScore. "+ e );
        }
    }

    private static String formatDate(final Date d) {
        final Format f = new SimpleDateFormat("HH:mm MM/dd/yyyy");
        return f.format(d);
    }

    public static String getScores(final String nick)
            throws IOException, FileNotFoundException {
        checkfile();
        String ss="";
        final BufferedReader in =
                new BufferedReader(new FileReader(HIGHSCORE_DAT));
        String data;
        while ((data = in.readLine()) != null) {
            // File format is nick\tfname\te-mail
            final String[] scoredata = data.split("\t");
            //"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
            if (nick.equals(scoredata[0])) {
                 ss = scoredata[0] + "\t" + scoredata[1] + "\t" + scoredata[2] + "\n";

            }
        }
        return ss;
    }

    public static String getvalues()
            throws IOException, FileNotFoundException {
        checkfile();
        String ss="";
        final BufferedReader in =
                new BufferedReader(new FileReader(HIGHSCORE_DAT));
        String data;
        while ((data = in.readLine()) != null) {
            final String[] scoredata = data.split("\t");
                ss += scoredata[0] + "\t" + scoredata[1] + "\t" + scoredata[2] + "\n";
        }
        return ss;
    }
    public static Vector gethighscores()
            throws IOException, FileNotFoundException {
        checkfile();
        final Vector ss= new Vector();
        final BufferedReader in =
                new BufferedReader(new FileReader(HIGHSCORE_DAT));
        String data;
        while ((data = in.readLine()) != null) {
             final String[] scoredata = data.split("\t");
                ss.add( scoredata[0] + ",   " + scoredata[1] + ",   " + scoredata[2] + "\n");
        }
        return ss;
    }
    public static Vector gethighoverallscores()
            throws IOException, FileNotFoundException {
        checkfile();
        int max=0;
        final Vector ss= new Vector();
        final BufferedReader in =
                new BufferedReader(new FileReader(HIGHSCORE_DAT));
        String data;
        while ((data = in.readLine()) != null) {
             final String[] scoredata = data.split("\t");
            int val=Integer.parseInt(scoredata[2].trim());  
             if(val >= max){
                max=val;
                }
        }
        final BufferedReader inn =
                new BufferedReader(new FileReader(HIGHSCORE_DAT));
        while ((data = inn.readLine()) != null) {
            final String[] scoredata = data.split("\t");
            int val=Integer.parseInt(scoredata[2].trim());
            if(val == max){
                ss.add( scoredata[0] + ",   " + scoredata[1] + ",   " + scoredata[2] + "\n");

            }
        }
        return ss;
}

}
