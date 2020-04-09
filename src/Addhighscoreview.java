import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.io.IOException;
import java.util.*;
public class Addhighscoreview implements ListSelectionListener{
    private ControlDeskView controlDesk;
    private JFrame win;
    private JList highscorelist,highoveralllist;

    public Addhighscoreview(ControlDeskView controlDesk) throws IOException {
        this.controlDesk=controlDesk;
        win = new JFrame("High Scores");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 2));
        //Panel_1
        JPanel panel_1=new JPanel();
        panel_1.setLayout(new FlowLayout());
        panel_1.setBorder(new TitledBorder("Highscore of eachplayer"));
        Vector highlist = new Vector();

        highlist=highscorefile.gethighscores();

        highscorelist=new JList(highlist);
        highscorelist.setFixedCellWidth(300);
        highscorelist.setVisibleRowCount(5);
        highscorelist.addListSelectionListener(this);
        JScrollPane pane_1 = new JScrollPane(highscorelist);
        panel_1.add(pane_1);
        //panel_2
        JPanel panel_2=new JPanel();
        panel_2.setLayout(new FlowLayout());
        panel_2.setBorder(new TitledBorder("Highscore(ALL Game)"));
        Vector highoverall = new Vector();

        highoverall=highscorefile.gethighoverallscores();
//        Vector final_high=
        highoveralllist=new JList(highoverall);
        highoveralllist.setFixedCellWidth(300);
        highoveralllist.setVisibleRowCount(5);
        highoveralllist.addListSelectionListener(this);
        JScrollPane pane_2 = new JScrollPane(highoveralllist);
        panel_2.add(pane_2);

        colPanel.add(panel_1);
        colPanel.add(panel_2);
//        colPanel.add(panel_3);
        win.getContentPane().add("Center", colPanel);
        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.show();
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {

    }
}