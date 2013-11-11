package magic.ui;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.viewer.CardViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.ZoneBackgroundLabel;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class VersionPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String VERSION = "1.44";
    private static final String VERSION_TEXT = "Magarena " + VERSION;

    private final MagicFrame frame;
    private final ZoneBackgroundLabel backgroundLabel;
    private final CardViewer cardViewer;
    private final JButton newButton;
    private final JButton loadButton;

    public VersionPanel(final MagicFrame frame) {

        this.frame=frame;
        frame.setTitle(VERSION_TEXT);

        setLayout(null);

        backgroundLabel=new ZoneBackgroundLabel();

        cardViewer=new CardViewer(true,true);
        final List<MagicCardDefinition> spellCards=CardDefinitions.getSpellCards();
        final int index=MagicRandom.nextRNGInt(spellCards.size());
        cardViewer.setCard(spellCards.get(index),0);

        newButton=new JButton("NEW");
        newButton.setFont(FontsAndBorders.FONT4);
        newButton.addActionListener(this);
        newButton.setFocusable(false);

        loadButton=new JButton("LOAD");
        loadButton.setFont(FontsAndBorders.FONT4);
        loadButton.addActionListener(this);
        loadButton.setFocusable(false);

        add(newButton);
        add(loadButton);
        add(cardViewer);
        add(backgroundLabel);

        addComponentListener(new ComponentAdapter()  {

            @Override
            public void componentResized(final ComponentEvent event) {

                final Dimension size=getSize();
                backgroundLabel.setSize(size);
                cardViewer.setLocation(size.width-cardViewer.getWidth()-10,size.height-cardViewer.getHeight()-10);
                newButton.setBounds(10,size.height-60,130,50);
                loadButton.setBounds(150,size.height-60,130,50);
            }
        });
    }

    public static final String getVersion() {
        return VERSION;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {

        final Object source=event.getSource();
        if (source==newButton) {
            frame.showNewDuelDialog();
        } else if (source==loadButton) {
            frame.loadDuel();
        }
    }
}
