import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ScoreBoard {

    private class Frame {
        public int[] thro;
        private boolean isSpare;
        private boolean isStrike;
        public int score;

        public Frame() {
            thro = new int[2];
            thro[0] = 0;
            thro[1] = 0;
            isSpare = false;
            isStrike = false;
            score = 0;
        }

        public void setScore(int throNum, int score) {
//            parameters known to be safe
            thro[throNum] = score;
            this.score += score;
            if (thro[0] == 10) {
                isStrike = true;
            } else if (thro[0] + thro[1] == 10) {
                isSpare = true;
            }
        }

        public boolean isSpare() {
            return isSpare;
        }

        public boolean isStrike() {
            return isStrike;
        }
    }

    private HashMap<Bowler, ArrayList<Frame>> nowPlaying; // bowler vs list of frames for this game
    private HashMap<Bowler, ArrayList<Integer>> allGames; // bowler vs list of scores across games
    private Party party;

    public ScoreBoard(Party party) {
        this.party = party;
        for (Iterator i = party.getMembers().iterator(); i.hasNext(); ) {
            allGames.put((Bowler) i.next(), new ArrayList<Integer>());
        }

        newGame();
    }

    public void newGame() {
        nowPlaying.clear();
        for (Iterator i = party.getMembers().iterator(); i.hasNext(); ) {
            nowPlaying.put((Bowler) i.next(), new ArrayList<Frame>(Util.FRAMES_PER_GAME));
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
        HashMap<Bowler, ArrayList<Frame>> nowPlayingCopy = nowPlaying;
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
}
