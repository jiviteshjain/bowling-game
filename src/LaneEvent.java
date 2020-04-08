/*  $Id$
 *
 *  Revisions:
 *    $Log: LaneEvent.java,v $
 *    Revision 1.6  2003/02/16 22:59:34  ???
 *    added mechnanical problem flag
 *
 *    Revision 1.5  2003/02/02 23:55:31  ???
 *    Many many changes.
 *
 *    Revision 1.4  2003/02/02 22:44:26  ???
 *    More data.
 *
 *    Revision 1.3  2003/02/02 17:49:31  ???
 *    Modified.
 *
 *    Revision 1.2  2003/01/30 21:21:07  ???
 *    *** empty log message ***
 *
 *    Revision 1.1  2003/01/19 22:12:40  ???
 *    created laneevent and laneobserver
 *
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

public class LaneEvent {

	private Party p;
	int ball;
	Bowler bowler;
	HashMap<Bowler, ArrayList<Frame>> currentScores;
	int index;
	int frameNum;

	boolean mechProb;
	
	public LaneEvent(Party pty, int theIndex, Bowler theBowler, int theFrameNum, int theBall, HashMap<Bowler, ArrayList<Frame>> currentScores, boolean mechProblem) {
		p = pty;
		index = theIndex;
		bowler = theBowler;
		this.currentScores = currentScores;
		frameNum = theFrameNum;
		ball = theBall;	
		mechProb = mechProblem;
	}

	public boolean isMechanicalProblem() {
		return mechProb;
	}
	
	public int getFrameNum() {
		return frameNum;
	}
	
	public HashMap<Bowler, ArrayList<Frame>> getScore( ) {
		return currentScores;
	}
	
	public int getIndex() {
		return index;
	}

	public int getBall( ) {
		return ball;
	}

	public Party getParty() {
		return p;
	}
	
	public Bowler getBowler() {
		return bowler;
	}

};
 
