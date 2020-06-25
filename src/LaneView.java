/*
 *  constructs a prototype Lane View
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class LaneView implements LaneObserver, ActionListener {

	private int roll;
	private boolean initDone;
	private boolean frameInitDone;

	JFrame frame;
	Container cpanel;
	Vector bowlers;
	int cur;
	Iterator bowlIt;

	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;

	JButton maintenance;
	Lane lane;

	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;

		initDone = true;
		frameInitDone = false;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});

		cpanel.add(new JPanel());

	}

	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}

	private JPanel makeFrame(Party party) {

		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][23];
		ballLabel = new JLabel[numBowlers][23];
		scores = new JPanel[numBowlers][10];
		scoreLabel = new JLabel[numBowlers][10];
		ballGrid = new JPanel[numBowlers][10];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 23; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 9; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			int j = 9;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}

		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(
				BorderFactory.createTitledBorder(
					((Bowler) bowlers.get(i)).getNickName()));
			pins[i].setLayout(new GridLayout(0, 10));
			for (int k = 0; k != 10; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	public void receiveLaneEvent(LaneEvent le) {
		if (lane.isPartyAssigned()) {
			int numBowlers = le.getParty().getMembers().size();
			while (!initDone) {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}


			if (!frameInitDone || (le.getFrameNum() == 1 && le.getBall() == 0 && le.getIndex() == 0)) {
				frameInitDone = true;
				System.out.println("Making the frame.");
				cpanel.removeAll();
				cpanel.add(makeFrame(le.getParty()), "Center");

				// Button Panel
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());

				Insets buttonMargin = new Insets(4, 4, 4, 4);

				maintenance = new JButton("Maintenance Call");
				JPanel maintenancePanel = new JPanel();
				maintenancePanel.setLayout(new FlowLayout());
				maintenance.addActionListener(this);
				maintenancePanel.add(maintenance);

				buttonPanel.add(maintenancePanel);

				cpanel.add(buttonPanel, "South");

				frame.pack();

			}

			HashMap<Bowler, ArrayList<Frame>> allScores = le.getScore();
			for (int b = 0; b < numBowlers; b++) {
				Bowler bowler = (Bowler) bowlers.get(b);
				ArrayList<Frame> bowlerScores = allScores.get(bowler);
				for (int f = 0; f < le.getFrameNum(); f++) {
					Frame frame = bowlerScores.get(f);
					scoreLabel[b][f].setText(String.valueOf(frame.score));
					if (frame.isStrike()) {
						ballLabel[b][2*f].setText("X");
						ballLabel[b][2*f+1].setText("");
					} else if (frame.isSpare()) {
						ballLabel[b][2*f].setText(String.valueOf(frame.thro[0]));
						ballLabel[b][2*f+1].setText("/");
					} else {
						ballLabel[b][2*f].setText(String.valueOf(frame.thro[0]));
						ballLabel[b][2*f + 1].setText(String.valueOf(frame.thro[1]));
					}

				}
			}



		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame();
		}
	}

}
