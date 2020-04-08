/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EndGamePrompt implements ActionListener {

	private final JFrame win;
	private final JButton yesButton, noButton;

	private int result;


	public EndGamePrompt( final String partyName ) {

		result =0;
		
		win = new JFrame("Another Game for " + partyName + "?" );
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		final JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout( 2, 1 ));

		// Label Panel
		final JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout());
		
		final JLabel message = new JLabel( "Party " + partyName 
			+ " has finished bowling.\nWould they like to bowl another game?" );

		labelPanel.add( message );

		// Button Panel
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		new Insets(4, 4, 4, 4);

		yesButton = new JButton("Yes");
		final JPanel yesButtonPanel = new JPanel();
		yesButtonPanel.setLayout(new FlowLayout());
		yesButton.addActionListener(this);
		yesButtonPanel.add(yesButton);

		noButton = new JButton("No");
		final JPanel noButtonPanel = new JPanel();
		noButtonPanel.setLayout(new FlowLayout());
		noButton.addActionListener(this);
		noButtonPanel.add(noButton);

		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);

		// Clean up main panel
		colPanel.add(labelPanel);
		colPanel.add(buttonPanel);

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		final Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		final int val1=getval(screenSize.width,win.getSize().width);
		final int val2=getval(screenSize.height,win.getSize().height);
		win.setLocation(val1,val2);
		win.show();

	}
	public int getval(final int a,final int b){
		return (a-b)/2;
	}



	public void actionPerformed(final ActionEvent e) {
		if (e.getSource().equals(yesButton)) {		
			result=1;
		}
		if (e.getSource().equals(noButton)) {		
			result=2;
		}

	}

	public int getResult() {
		while ( result == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( final InterruptedException e ) {
				System.err.println( "Interrupted" );
			}
		}
		return result;	
	}
	
	public void distroy() {
		win.hide();
	}
	
}

