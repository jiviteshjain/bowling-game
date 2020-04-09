import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseAndPlayView implements ActionListener {

    JFrame window;
    JButton load, cancel;
    JList contextList;
    PauseAndPlay pauseAndPlay;


    public PauseAndPlayView(PauseAndPlay pauseAndPlay) {
        this.pauseAndPlay = pauseAndPlay;

//        window
        window = new JFrame("Load Saved Games");
        window.getContentPane().setLayout(new BorderLayout());
        ((JPanel)window.getContentPane()).setOpaque(false);

//        window panel
        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 2));

//        list panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new FlowLayout());
        listPanel.setBorder(new TitledBorder("Choose games to load"));

        String[] reprArr = pauseAndPlay.getNames();
        if (reprArr.length == 0) {
            return;
        }

        this.contextList = new JList(reprArr);
        this.contextList.setFixedCellWidth(120);
        this.contextList.setVisibleRowCount(5);

        JScrollPane listPane = new JScrollPane(this.contextList);
        listPanel.add(listPane);

//        button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));

        this.load = new JButton("Load");
        JPanel loadPanel = new JPanel();
        loadPanel.setLayout(new FlowLayout());
        this.load.addActionListener(this);
        loadPanel.add(this.load);

        this.cancel = new JButton("Cancel");
        JPanel cancelPanel = new JPanel();
        cancelPanel.setLayout(new FlowLayout());
        this.cancel.addActionListener(this);
        cancelPanel.add(this.cancel);
        
        buttonPanel.add(loadPanel);
        buttonPanel.add(cancelPanel);
        
//        pack
        colPanel.add(listPanel);
        colPanel.add(buttonPanel);
        this.window.getContentPane().add("Center", colPanel);
        this.window.pack();
        this.window.setAlwaysOnTop(true);
        this.window.setAutoRequestFocus(true);

//        add focus listener
//        this.window.addWindowFocusListener(new WindowFocusListener() {
//
//            @Override
//            public void windowGainedFocus(WindowEvent windowEvent) {
//            }
//
//            @Override
//            public void windowLostFocus(WindowEvent windowEvent) {
//                pauseAndPlay.noop();
//                windowEvent.getWindow().setVisible(false);
//            }
//        });

//        show
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        this.window.setLocation(
                ((screenSize.width) / 2) - ((this.window.getSize().width) / 2),
                ((screenSize.height) / 2) - ((this.window.getSize().height) / 2));
        this.window.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.cancel)) {
            this.window.setVisible(false);
            pauseAndPlay.noop();
        } else if (e.getSource().equals(this.load)) {

            this.window.setVisible(false);
            int[] selectedContextIndices = this.contextList.getSelectedIndices();
            pauseAndPlay.load(selectedContextIndices);
        }
    }
}
