import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class Util {
    public static final int FRAMES_PER_GAME = 10;
    public static final int GAMES_PER_LANE = 128;

    public static int[] convertIntegers(ArrayList<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    public static ArrayList<Frame> copyFrameArrayList(ArrayList<Frame> oldList) {
        ArrayList<Frame> newList = new ArrayList<Frame>();
        for (Frame f: oldList) {
            newList.add(f.clone());
        }
        return newList;
    }

    public static HashMap<Bowler, ArrayList<Frame>> copyFrameHashMap(HashMap<Bowler, ArrayList<Frame>> oldMap) {
        HashMap<Bowler, ArrayList<Frame>> newMap = new HashMap<Bowler, ArrayList<Frame>>();
        for (Iterator i = oldMap.entrySet().iterator(); i.hasNext(); ) {
            HashMap.Entry me = ((HashMap.Entry)i.next());
            ArrayList<Frame> oldArray = (ArrayList<Frame>) me.getValue();
            ArrayList<Frame> newArray = Util.copyFrameArrayList(oldArray);
            Bowler b = (Bowler) me.getKey();
            newMap.put(b, newArray);
        }
        return newMap;
    }
}
