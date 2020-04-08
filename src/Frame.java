public class Frame {
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

    public void setSpare(boolean spare) {
        isSpare = spare;
    }

    public void setStrike(boolean strike) {
        isStrike = strike;
    }

    public boolean isSpare() {
        return isSpare;
    }

    public boolean isStrike() {
        return isStrike;
    }

    public Frame clone() {
        Frame f = new Frame();
        f.thro[0] = this.thro[0];
        f.thro[1] = this.thro[1];
        f.score = this.score;
        f.setSpare(this.isSpare);
        f.setStrike(this.isStrike);
        return f;
    }
}
