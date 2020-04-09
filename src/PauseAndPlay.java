import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static java.lang.Thread.sleep;

public class PauseAndPlay {

    private HashSet<Lane> lanes;
    private final String FILE_NAME = "SAVEDGAMES.DAT";
    private ArrayList<HashMap<String, Object>> contexts;

    public PauseAndPlay(HashSet<Lane> lanes) {
        this.lanes = lanes;
        this.contexts = getAvailable();
        noop();
    }

    public void save(Lane lane) {
        lane.pauseGame();
        try {
            sleep(2000); // frame should end by now
        } catch (InterruptedException ignored) {}

        String s = stringify(lane.getContext());
        try {
            fileWrite(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String stringify(HashMap<String, Object> context) {

        Party party = (Party) context.get("party");
        Vector members = party.getMembers();

        String memberString = String.valueOf(members.size()).concat("\n");
        for (Object member: members) {
            memberString = memberString.concat(((Bowler)member).getNickName()).concat("\n");
        }

        ScoreBoard scoreBoard = (ScoreBoard)context.get("scoreboard");
        HashMap<Bowler, ArrayList<Frame>> scoreBoardCurrent = scoreBoard.getRawCurrentGame();
        HashMap<Bowler, ArrayList<Integer>> scoreBoardAll = scoreBoard.getAllGames();

        String scoreBoardString = stringifyScoreBoardCurrent(scoreBoardCurrent);
        scoreBoardString = scoreBoardString.concat(stringifyScoreBoardAll(scoreBoardAll));

        String extraString = String.format("%d %d %d\n",
                context.get("bowlIndex"),
                context.get("frameNumber"),
                context.get("gameNumber")
        );

        return memberString.concat(scoreBoardString).concat(extraString);
    }

    private void fileWrite(String s) throws IOException {
        RandomAccessFile out = new RandomAccessFile(FILE_NAME, "rw");
        out.skipBytes((int) out.length());
        out.writeBytes(s);
        out.close();
    }

    private String stringifyScoreBoardCurrent(HashMap<Bowler, ArrayList<Frame>> scoreBoardCurrent) {
        String repr = "";

        for (Object o: scoreBoardCurrent.entrySet()) {

            Bowler b = (Bowler) ((HashMap.Entry)o).getKey();
            ArrayList<Frame> a = (ArrayList<Frame>) ((HashMap.Entry)o).getValue();

            repr = repr.concat(b.getNickName()).concat("\n").concat(String.valueOf(a.size())).concat("\n");

            for (Frame f: a) {
                char isSpare = (f.isSpare() ? '1' : '0');
                char isStrike = (f.isStrike() ? '1' : '0');

                String frepr = String.format("%d %d %d %c %c\n", f.thro[0], f.thro[1], f.score, isSpare, isStrike);
                repr = repr.concat(frepr);
            }
        }
        return repr;
    }

    private String stringifyScoreBoardAll(HashMap<Bowler, ArrayList<Integer>> scoreBoardAll) {
        String repr = "";

        for (Object o: scoreBoardAll.entrySet()) {

            Bowler b = (Bowler) ((HashMap.Entry)o).getKey();
            ArrayList<Integer> a = (ArrayList<Integer>) ((HashMap.Entry)o).getValue();

            repr = repr.concat(b.getNickName()).concat("\n").concat(String.valueOf(a.size())).concat("\n");

            for (int i: a) {
                repr = repr.concat(String.valueOf(i)).concat("\n");
            }
        }
        return repr;
    }

    private HashMap<String, Object> parseContext(BufferedReader in) throws IOException, IllegalStateException {
//        parses a single context for a single lane
        HashMap<String, Object> context = new HashMap<String, Object>();
        try {
//            get party
            int numBowlers = Integer.parseInt(in.readLine());
            Vector<Bowler> bowlers = new Vector<Bowler>();

            for (int i = 0; i < numBowlers; i++) {
                String nick = in.readLine();
                bowlers.add(BowlerFile.getBowlerInfo(nick));
            }
            Party party = new Party(bowlers);
            context.put("party", party);

//            create party's display name
            String repr = bowlers.get(0).getNickName();
            repr = repr.concat("'s Party");
            context.put("repr", repr);

//            get scoreBoardCurrent
            HashMap<Bowler, ArrayList<Frame>> scoreBoardCurrent = new HashMap<Bowler, ArrayList<Frame>>();

            for (int i = 0; i < numBowlers; i++) {
                Bowler b = party.getBowler(in.readLine());
                int listSize = Integer.parseInt(in.readLine());

                ArrayList<Frame> list = new ArrayList<Frame>();
                for (int j = 0; j < listSize; j++) {
                    list.add(parseFrame(in.readLine()));
                }
                scoreBoardCurrent.put(b, list);
            }

//            get scoreBoardAll
            HashMap<Bowler, ArrayList<Integer>> scoreBoardAll = new HashMap<Bowler, ArrayList<Integer>>();

            for (int i = 0; i < numBowlers; i++) {
                Bowler b = party.getBowler(in.readLine());
                int listSize = Integer.parseInt(in.readLine());

                ArrayList<Integer> list = new ArrayList<Integer>();
                for (int j = 0; j < listSize; j++) {
                    list.add(Integer.parseInt(in.readLine()));
                }
                scoreBoardAll.put(b, list);
            }

            ScoreBoard scoreBoard = new ScoreBoard(party);
            scoreBoard.setRaw(scoreBoardCurrent, scoreBoardAll);
            context.put("scoreboard", scoreBoard);

            String[] split = in.readLine().trim().split("\\s+");
            context.put("bowlIndex", Integer.parseInt(split[0]));
            context.put("frameNumber", Integer.parseInt(split[1]));
            context.put("gameNumber", Integer.parseInt(split[2]));
            return context;


        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private ArrayList<HashMap<String, Object>> getAvailable() {
        ArrayList<HashMap<String, Object>> contexts = new ArrayList<HashMap<String, Object>>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(FILE_NAME));
            while (true) {
                HashMap<String, Object> context = parseContext(in);
                contexts.add(context);
            }
        } catch (Exception e) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return contexts;
        }
    }

    private Frame parseFrame(String s) throws IllegalStateException {
        try {
            Frame f = new Frame();
            String[] split = s.trim().split("\\s+");

            f.thro[0] = Integer.parseInt(split[0]);
            f.thro[1] = Integer.parseInt(split[1]);
            f.score = Integer.parseInt(split[2]);
            boolean isSpare = Integer.parseInt(split[3]) == 1;
            boolean isStrike = Integer.parseInt(split[4]) == 1;
            f.setSpare(isSpare);
            f.setStrike(isStrike);
            return f;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public String[] getNames() {
        String[] reprArr = new String[contexts.size()];
        for (int i = 0; i < contexts.size(); i++) {
            reprArr[i] = (String) contexts.get(i).get("repr");
        }
        return reprArr;
    }

    public void load(int[] selectedContextIndices) throws IllegalStateException {
        ArrayList<HashMap<String, Object>> selectedContexts = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < selectedContextIndices.length; i++) {
            selectedContexts.add(this.contexts.get(selectedContextIndices[i]));
        }

        if (selectedContexts.size() > lanes.size()) {
            throw new IllegalStateException("More saved games than lanes");
        }

        Iterator laneIt = lanes.iterator();
        for (Object c: selectedContexts) {
            HashMap<String, Object> context = (HashMap<String, Object>) c;
            Lane lane = null;
            while (laneIt.hasNext()) {
                lane = (Lane) laneIt.next();
                if (!lane.isPartyAssigned()) {
                    break;
                } else {
                    lane = null;
                }
            }
            if (lane == null) {
                throw new IllegalStateException("More saved games than available lanes");
            }

            lane.setContext(context);
//            lane.unPauseGame();
        }
    }

    public void noop() {
        File f = new File(FILE_NAME);
        try {
            Files.deleteIfExists(f.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePaused() {
        for (Lane lane: lanes) {
            if (lane.isPaused()) {
                save(lane);
            }
        }
    }
}
