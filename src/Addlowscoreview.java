import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.io.IOException;
import java.util.*;
public class Addlowscoreview implements ListSelectionListener{
    private ControlDeskView controlDesk;
    private JFrame win;
    private JList lowscorelist,lowoveralllist;

    public Addlowscoreview(ControlDeskView controlDesk) throws IOException {
        this.controlDesk=controlDesk;
        win = new JFrame("low Scores");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 2));
        //Panel_1
        JPanel panel_1=new JPanel();
        panel_1.setLayout(new FlowLayout());
        panel_1.setBorder(new TitledBorder("lowscore of eachplayer"));
        Vector lowlist = new Vector();

        lowlist=lowscorefile.getlowscores();

        lowscorelist=new JList(lowlist);
        lowscorelist.setFixedCellWidth(300);
        lowscorelist.setVisibleRowCount(5);
        lowscorelist.addListSelectionListener(this);
        JScrollPane pane_1 = new JScrollPane(lowscorelist);
        panel_1.add(pane_1);
        //panel_2
       JPanel panel_2=new JPanel();
       panel_2.setLayout(new FlowLayout());
       panel_2.setBorder(new TitledBorder("lowscore(ALL Game)"));
       Vector lowoverall = new Vector();

       lowoverall=lowscorefile.getlowoverallscores();
       lowoveralllist=new JList(lowoverall);
       lowoveralllist.setFixedCellWidth(300);
       lowoveralllist.setVisibleRowCount(5);
       lowoveralllist.addListSelectionListener(this);
       JScrollPane pane_2 = new JScrollPane(lowoveralllist);
       panel_2.add(pane_2);

        colPanel.add(panel_1);
       colPanel.add(panel_2);
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