package magic.ui;

import magic.data.IconImages;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DuelDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final MagicFrame frame;
    private final JButton okButton;
    private final JButton cancelButton;
    private final DuelSetupPanel duelSetupPanel;

    public DuelDialog(final MagicFrame frame) {

        super(frame,true);
        this.frame=frame;
        this.setTitle("New duel");
        this.setSize(500,500);
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JPanel buttonPanel=new JPanel();
        buttonPanel.setPreferredSize(new Dimension(0,45));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,15,0));
        okButton=new JButton("OK");
        okButton.setFocusable(false);
        okButton.setIcon(IconImages.OK);
        okButton.addActionListener(this);
        buttonPanel.add(okButton);
        cancelButton=new JButton("Cancel");
        cancelButton.setFocusable(false);
        cancelButton.setIcon(IconImages.CANCEL);
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        duelSetupPanel = new DuelSetupPanel(frame);
        duelSetupPanel.setOpaque(false);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(duelSetupPanel,BorderLayout.CENTER);
        getContentPane().add(buttonPanel,BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(final ActionEvent event) {

        final Object source=event.getSource();
        if (source==okButton) {
            frame.newDuel(duelSetupPanel.getNewDuelConfig());
            dispose();
        } else if (source==cancelButton) {
            dispose();
        }
    }

}
