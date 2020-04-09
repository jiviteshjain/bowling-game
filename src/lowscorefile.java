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

public class lowscorefile {
    public int max_value;
    private static String LOWSCORE_DAT = "LOWSCORE.DAT";

    private static void _addScorelow(final String nick, final Date date, final int score)
            throws IOException, FileNotFoundException {
        final String s = String.valueOf(score);
        final String d = formatDate(date);
        final String data = nick + "\t" + d + "\t" + s + "\n";
        String ss;
        String val;
        ss=getScores(nick);
//        String[] low=ss.split("t");
//        int max_val;
//        max_val=Integer.parseInt(low[2]);
//        System.out.println("max"+max_val);
        if(ss.isEmpty()){
        final RandomAccessFile out = new RandomAccessFile(LOWSCORE_DAT, "rw");
        out.skipBytes((int) out.length());
        out.writeBytes(data);
        out.close();}
        else{
            final String[] scoredata = data.split("\t");
            int max_val;
            max_val=Integer.parseInt(scoredata[2].trim());
//            System.out.println("max"+scoredata[2]);
            if(score<=max_val) {
                val = getvalues();
                val = val.replaceAll(ss, data);
                final RandomAccessFile out = new RandomAccessFile(LOWSCORE_DAT, "rw");
                out.writeBytes(val);
                out.close();
            }
        }
    }
    public static void checkfile(){
        try {
            File myObj = new File("LOWSCORE.DAT");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void addScorelow(final String nick, final Date date, final int score) {
        try{
            checkfile();
            _addScorelow(nick, date, score);
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
//        Vector scores = new Vector();
        checkfile();
        String ss="";
        final BufferedReader readlowfile =
                new BufferedReader(new FileReader(LOWSCORE_DAT));
        String data;
        while ((data = readlowfile.readLine()) != null) {
            // File format is nick\tfname\te-mail
            final String[] scoredata = data.split("\t");
            //"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
            if (nick.equals(scoredata[0])) {
//                scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
                 ss = scoredata[0] + "\t" + scoredata[1] + "\t" + scoredata[2] + "\n";

            }
        }
        return ss;
    }

    public static String getvalues()
            throws IOException, FileNotFoundException {
        checkfile();
        String ss="";
        final BufferedReader readlowfile =
                new BufferedReader(new FileReader(LOWSCORE_DAT));
        String data;
        while ((data = readlowfile.readLine()) != null) {
            final String[] scoredata = data.split("\t");
                ss += scoredata[0] + "\t" + scoredata[1] + "\t" + scoredata[2] + "\n";
        }
        return ss;
    }
    public static Vector getlowscores()
            throws IOException, FileNotFoundException {
        checkfile();
        final Vector ss= new Vector();
        final BufferedReader readlowfile =
                new BufferedReader(new FileReader(LOWSCORE_DAT));
        String data;
        while ((data = readlowfile.readLine()) != null) {
             final String[] scoredata = data.split("\t");
             System.out.println("umesh"+data);
                ss.add( scoredata[0] + ",   " + scoredata[1] + ",   " + scoredata[2] + "\n");
        }
        return ss;
    }
    public static Vector getlowoverallscores()
            throws IOException, FileNotFoundException {
        //        String ss="";
        checkfile();
        int max=10000;
        final Vector ss= new Vector();
        final BufferedReader readlowfile =
                new BufferedReader(new FileReader(LOWSCORE_DAT));
        String data;
        while ((data = readlowfile.readLine()) != null) {
             final String[] scoredata = data.split("\t");
            int val=Integer.parseInt(scoredata[2].trim());  
             if(val <= max){
                max=val;
                }
        }
        final BufferedReader readlowfilen =
                new BufferedReader(new FileReader(LOWSCORE_DAT));
        while ((data = readlowfilen.readLine()) != null) {
            final String[] scoredata = data.split("\t");
            int val=Integer.parseInt(scoredata[2].trim());
            if(val == max){
                ss.add( scoredata[0] + ",   " + scoredata[1] + ",   " + scoredata[2] + "\n");

            }
        }
        return ss;
}

}
