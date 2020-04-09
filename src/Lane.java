
/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 * 
 */

import java.util.*;

public class Lane extends Thread implements PinsetterObserver {	
	private Party party;
	private Pinsetter setter;
	private ScoreBoard scoreboard;
	private boolean currentThrowIsLastFrameExtra;
	private int lastFrameExtraTotal;
	private int lastFrameExtraPlayed;
	private Vector subscribers;

	private boolean gameIsHalted;

	private boolean partyAssigned;
	private boolean gameFinished;
	private Iterator bowlerIterator;
	private int ball;
	private int bowlIndex;
	private int frameNumber;


	private boolean canThrowAgain;
	
	private int gameNumber;
	
	private Bowler currentThrower;			// = the thrower who just took a throw

	/** Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */
	public Lane() {
		setter = new Pinsetter();
		subscribers = new Vector();

		gameIsHalted = false;
		partyAssigned = false;

		gameNumber = 0;

		setter.subscribe( this );
		
		this.start();
	}

	/** run()
	 * 
	 * entry point for execution of this lane 
	 */
	public void run() {
		
		while (true) {
			if (partyAssigned && !gameFinished) {	// we have a party on this lane, 
								// so next bower can take a throw
			
				while (gameIsHalted) {
					try {
						sleep(10);
					} catch (Exception ignored) {}
				}


				if (bowlerIterator.hasNext()) {
					currentThrower = (Bowler)bowlerIterator.next();

					canThrowAgain = true;
					lastFrameExtraPlayed = 0;
					lastFrameExtraTotal = 0;
					currentThrowIsLastFrameExtra = false;

					ball = 0;
					while (canThrowAgain) {
						setter.ballThrown();		// simulate the thrower's ball hiting
						ball++;
					}
					
					if (frameNumber == 9){
						int score = scoreboard.getCurrentGame(currentThrower).get(Util.FRAMES_PER_GAME-1).score;
						String nick=currentThrower.getNickName();
						Date date= new Date();
						ScoreHistoryFile.addScore(nick,date, score);
						highscorefile.addScore(nick,date, score);
						lowscorefile.addScorelow(nick, date, score);
					}

					
					setter.reset();
					bowlIndex++;
					
				} else {
					frameNumber++;
					bowlerIterator = (party.getMembers()).iterator();
					bowlIndex = 0;
					if (frameNumber > 9) {
						gameFinished = true;
						gameNumber++;
					}
				}
			} else if (partyAssigned && gameFinished) {
				EndGamePrompt egp = new EndGamePrompt( ((Bowler) party.getMembers().get(0)).getNickName() + "'s Party" );
				int result = egp.getResult();
				egp.destroy();

				scoreboard.saveGame();
				System.out.println("result was: " + result);
				
				// TODO: send record of scores to control desk
				if (result == 1) {// yes, want to play again
					scoreboard.newGame();
					gameFinished = false;
					frameNumber = 0;
					bowlerIterator = (party.getMembers()).iterator();
					
				} else if (result == 2) {// no, dont want to play another game

					sendReport();
					partyAssigned = false;
					party = null;
					publish();

				}
			}
			
			
			try {
				sleep(10);
			} catch (Exception e) {}
		}
	}

	private void sendReport() {
		EndGameReport egr = new EndGameReport( ((Bowler)party.getMembers().get(0)).getNickName() + "'s Party", party);
		Vector printVector = egr.getResult();

		for (Object o : party.getMembers()) {
			Bowler thisBowler = (Bowler) o;
			ArrayList<Integer> temp = scoreboard.getAllGames(thisBowler);
			ScoreReport sr = new ScoreReport(thisBowler, Util.convertIntegers(temp), gameNumber);
			sr.sendEmail(thisBowler.getEmail());
			for (Object value : printVector) {
				if (thisBowler.getNickName().equals(value)) {
					sr.sendPrintout();
				}
			}
		}
	}

	/** recievePinsetterEvent()
	 * 
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desiered
	 * 
	 * @param e 		The pinsetter event that has been received.
	 */
	public void receivePinsetterEvent(PinsetterEvent e) {
		if (e.pinsDownOnThisThrow() < 0) {
			return;
		}
		markScore(currentThrower, frameNumber, e.getThrowNumber()-1, e.pinsDownOnThisThrow());

		if (currentThrowIsLastFrameExtra) {
			lastFrameExtraPlayed++;
			if (lastFrameExtraPlayed >= lastFrameExtraTotal) {
				currentThrowIsLastFrameExtra = false;
				canThrowAgain = false;
				lastFrameExtraTotal = 0;
				lastFrameExtraPlayed = 0;
			}
			return;
		}

		if (frameNumber == 9) {
//			could be the end of the game

			if (e.totalPinsDown() == 10) {
//				extra chances, manually reset the setter
				setter.reset();
				currentThrowIsLastFrameExtra = true;
				canThrowAgain = true;
				lastFrameExtraPlayed = 0;
				if (e.getThrowNumber() == 1) {
					lastFrameExtraTotal = 2; // strike
				} else {
					lastFrameExtraTotal = 1; // spare
				}
			} else {
				canThrowAgain = false;
//				this player's turn is over, setter will automatically be reset
			}
			return;
		}

		if (e.totalPinsDown() == 10 || e.getThrowNumber() == 2) {
			canThrowAgain = false;
//			this player's turn is over, setter will automatically be reset
		}
	}



		
	/** assignParty()
	 * 
	 * assigns a party to this lane
	 * 
	 * @pre none
	 * @post the party has been assigned to the lane
	 * 
	 * @param theParty		Party to be assigned
	 */
	public void assignParty( Party theParty ) {
		party = theParty;
		bowlerIterator = (party.getMembers()).iterator();
		partyAssigned = true;
		scoreboard = new ScoreBoard(party);
		scoreboard.newGame();



		gameNumber = 0;
		gameFinished = false;
		frameNumber = 0;
		
	}

	/** markScore()
	 *
	 * Method that marks a bowlers score on the board.
	 * 
	 * @param bowler		The current bowler
	 * @param frameNum	The frame that bowler is on
	 * @param throNum		The ball the bowler is on
	 * @param score	The bowler's score 
	 */
	private void markScore(Bowler bowler, int frameNum, int throNum, int score) {
		if (currentThrowIsLastFrameExtra) {
			scoreboard.lastFrameExtra(bowler, score);
		} else {
			scoreboard.registerScore(bowler, frameNum, throNum, score);
		}
		publish();
	}


	/** isPartyAssigned()
	 * 
	 * checks if a party is assigned to this lane
	 * 
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}

	/** subscribe
	 * 
	 * Method that will add a subscriber
	 * 
	 * @param adding	Observer that is to be added
	 */

	public void subscribe( LaneObserver adding ) {
		subscribers.add( adding );
	}


	/** publish
	 *
	 * Method that publishes an event to subscribers
	 *
	 */

	public void publish() {
		if( subscribers.size() > 0 ) {
			LaneEvent laneEvent = new LaneEvent(party, bowlIndex, currentThrower, frameNumber+1, ball, scoreboard.getCurrentGame(), gameIsHalted);

			for (Object subscriber : subscribers) {
				((LaneObserver) subscriber).receiveLaneEvent(laneEvent);
			}
		}
	}

	/**
	 * Accessor to get this Lane's pinsetter
	 * 
	 * @return		A reference to this lane's pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;	
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		publish();
	}

	public boolean isPaused() {
		return gameIsHalted;
	}
	
	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		publish();
	}

	public HashMap<String, Object> getContext() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("party", this.party);
		map.put("scoreboard", this.scoreboard);
		map.put("bowlIndex", this.bowlIndex);
		map.put("frameNumber", this.frameNumber);
		map.put("gameNumber", this.gameNumber);

		return map;
	}

	public void setContext(HashMap<String, Object> context) {
		this.gameIsHalted = true; // to prevent race conditions
		this.party = (Party) context.get("party");
		this.partyAssigned = true;

		this.scoreboard = (ScoreBoard) context.get("scoreboard");

		this.gameFinished = false;

		this.bowlIndex = (int) context.get("bowlIndex");
		this.bowlerIterator = (party.getMembers()).iterator();
		for (int i = 0; i < bowlIndex && bowlerIterator.hasNext(); i++) {
			bowlerIterator.next();
		}

		this.frameNumber = (int) context.get("frameNumber");
		this.gameNumber = (int) context.get("gameNumber");

		this.gameIsHalted = false;
	}
}
