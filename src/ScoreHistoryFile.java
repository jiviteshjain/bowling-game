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

public class ScoreHistoryFile {

	private static String SCOREHISTORY_DAT = "SCOREHISTORY.DAT";

	private static void _addScore(String nick, Date date, int score)
		throws IOException, FileNotFoundException {
		String s = String.valueOf(score);
		String d = formatDate(date);
		String data = nick + "\t" + d + "\t" + s + "\n";

		RandomAccessFile out = new RandomAccessFile(SCOREHISTORY_DAT, "rw");
		out.skipBytes((int) out.length());
		out.writeBytes(data);
		out.close();
	}

	public static void addScore(String nick, Date date, int score) {
		try{
			_addScore(nick, date, score);
		} catch (Exception e) {
			System.err.println("Exception in addScore. "+ e );
		}
	}

	private static String formatDate(Date d) {
		Format f = new SimpleDateFormat("HH:mm MM/dd/yyyy");
		return f.format(d);
	}

	public static Vector getScores(String nick)
		throws IOException, FileNotFoundException {
		Vector scores = new Vector();

		BufferedReader in =
			new BufferedReader(new FileReader(SCOREHISTORY_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is nick\tfname\te-mail
			String[] scoredata = data.split("\t");
			//"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
			if (nick.equals(scoredata[0])) {
				scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
			}
		}
		return scores;
	}

}
