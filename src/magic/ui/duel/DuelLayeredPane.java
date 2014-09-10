package magic.ui.duel;

import magic.ui.duel.viewer.CardViewer;
import magic.ui.widget.ZoneBackgroundLabel;

import javax.swing.JLayeredPane;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@SuppressWarnings("serial")
public class DuelLayeredPane extends JLayeredPane {

    final ZoneBackgroundLabel backgroundLabel;
    final DuelPanel gamePanel;

    public DuelLayeredPane(final DuelPanel gamePanel,final ZoneBackgroundLabel backgroundLabel) {

        this.backgroundLabel = backgroundLabel;
        this.gamePanel = gamePanel;

        setLayout(null);

        backgroundLabel.setLocation(0,0);
        add(backgroundLabel);
        setLayer(backgroundLabel,0);

        gamePanel.setLocation(0,0);
        add(gamePanel);
        setLayer(gamePanel,1);

        final CardViewer cardViewer=gamePanel.getImageCardViewer();
        add(cardViewer);
        setLayer(cardViewer,2);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent event) {
                updateView();
            }
        });
    }

    public void updateView() {
        final Dimension size=getSize();
        backgroundLabel.setSize(size);
        gamePanel.setSize(size);
        gamePanel.resizeComponents();
        repaint();
    }
}
