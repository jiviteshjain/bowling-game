import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ScoreBoard {

    private HashMap<Bowler, ArrayList<Frame>> nowPlaying; // bowler vs list of frames for this game
    private HashMap<Bowler, ArrayList<Integer>> allGames; // bowler vs list of scores across games
    private Party party;

    public ScoreBoard(Party party) {
        nowPlaying = new HashMap<Bowler, ArrayList<Frame>>();
        allGames = new HashMap<Bowler, ArrayList<Integer>>();
        this.party = party;
        for (Iterator i = party.getMembers().iterator(); i.hasNext(); ) {
            allGames.put((Bowler) i.next(), new ArrayList<Integer>());
        }

        newGame();
    }

    public void newGame() {
        nowPlaying.clear();
        for (Iterator i = party.getMembers().iterator(); i.hasNext(); ) {
            ArrayList<Frame> temp = new ArrayList<Frame>(Util.FRAMES_PER_GAME);
            for (int j = 0; j < Util.FRAMES_PER_GAME; j++) {
                temp.add(new Frame());
            }
            nowPlaying.put((Bowler) i.next(), temp);
        }
    }

    public void registerScore(Bowler bowler, int frameNum, int throNum, int score) {
        if (frameNum >= Util.FRAMES_PER_GAME || frameNum < 0) {
            throw new IllegalArgumentException("Frame number out of range");
        }

        if (throNum >= 2 || throNum < 0) {
            throw new IllegalArgumentException("Throw number out of range");
        }

        if (score > 10 || score < 0) {
            throw new IllegalArgumentException("Score out of range");
        }

        if (!party.getMembers().contains(bowler)) {
            throw new IllegalArgumentException("Bowler not in party");
        }

        ArrayList<Frame> scores = (ArrayList<Frame>) nowPlaying.get(bowler);
        Frame frame = scores.get(frameNum);

        if (frame.thro[throNum] != 0) {
            throw new IllegalStateException("This throw has already been played");
        }

        if (throNum == 1 && frame.isStrike()) {
            throw new IllegalStateException("A strike does not get a second score");
        }

        frame.setScore(throNum, score);

        if (frameNum == 0) {
            return;
        }

//        Spares
        if (throNum == 0) {
            if (scores.get(frameNum-1).isSpare()) {
                scores.get(frameNum-1).score += score;
            }
        }

//        Strikes
        if (scores.get(frameNum-1).isStrike()) {
            scores.get(frameNum-1).score += score;
        }
//        Handle case of double strike
        if (throNum == 0 && frameNum > 1) {
            if (scores.get(frameNum-1).isStrike() && scores.get(frameNum-2).isStrike()) {
                scores.get(frameNum-2).score += score;
            }
        }
    }

    public void lastFrameExtra(Bowler bowler, int score) {
        if (score > 10 || score < 0) {
            throw new IllegalArgumentException("Score out of range");
        }

        if (!party.getMembers().contains(bowler)) {
            throw new IllegalArgumentException("Bowler not in party");
        }

        Frame frame = nowPlaying.get(bowler).get(Util.FRAMES_PER_GAME-1);
        if(!frame.isStrike() && !frame.isSpare()) {
            throw new IllegalStateException("Last frame does not require extra throws");
        }

        frame.score += score;
    }

    public HashMap<Bowler, ArrayList<Frame>> getCurrentGame() {
        HashMap<Bowler, ArrayList<Frame>> nowPlayingCopy = Util.copyFrameHashMap(nowPlaying);
        for (Iterator i = nowPlayingCopy.entrySet().iterator(); i.hasNext(); ) {
            ArrayList<Frame> scores = (ArrayList<Frame>) ((HashMap.Entry)i.next()).getValue();
            for (int j = 1; j < scores.size(); j++) {
                scores.get(j).score += scores.get(j-1).score;
            }
        }
        return nowPlayingCopy;
    }

    public ArrayList<Frame> getCurrentGame(Bowler bowler) {
        return getCurrentGame().get(bowler);
    }

    public HashMap<Bowler, ArrayList<Integer>> getAllGames() {
        return allGames;
    }

    public ArrayList<Integer> getAllGames(Bowler bowler) {
        return allGames.get(bowler);
    }

    public void saveGame() {
        HashMap<Bowler, ArrayList<Frame>> finalScores = getCurrentGame();

        for (Iterator i = allGames.entrySet().iterator(); i.hasNext(); ) {
            HashMap.Entry<Bowler, ArrayList<Integer>> me = (HashMap.Entry<Bowler, ArrayList<Integer>>) i.next();
            Bowler bowler = me.getKey();

            int theirScore = finalScores.get(bowler).get(Util.FRAMES_PER_GAME - 1).score;

            me.getValue().add(theirScore);
        }
    }

    public HashMap<Bowler, ArrayList<Frame>> getRawCurrentGame() {
        return Util.copyFrameHashMap(nowPlaying);
    }

    public void setRaw(HashMap<Bowler, ArrayList<Frame>> nowPlaying, HashMap<Bowler, ArrayList<Integer>> allGames) {
//        has to match with party
        this.nowPlaying = Util.copyFrameHashMap(nowPlaying);
        this.allGames.clear();
        this.allGames.putAll(allGames);
    }
}
