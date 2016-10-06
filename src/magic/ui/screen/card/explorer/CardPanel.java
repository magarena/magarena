package magic.ui.screen.card.explorer;

import magic.ui.widget.card.decks.CardDecksPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.widget.duel.viewer.CardViewer;
import magic.ui.dialog.prefs.ImageSizePresets;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "decks: %d";
    private static final String _S2 = "List of decks containing this card";
    private static final String _S3 = "Double-click deck name to view complete deck.";
    private static final String _S4 = "searching...";

    private final MigLayout layout = new MigLayout();
    private final CardViewer cardViewer;
    private final JScrollPane cardScrollPane;
    private final CardDecksPanel decksPanel = new CardDecksPanel();
    private final SplitterButton decksButton = new SplitterButton(UiString.get(_S1, 0));
    private boolean isImageVisible = true;

    public CardPanel() {

        setOpaque(false);

        // card image viewer
        cardViewer = new CardViewer();

        // card image scroll pane
        cardScrollPane = new JScrollPane();
        cardScrollPane.setViewportView(cardViewer);
        cardScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        cardScrollPane.setOpaque(false);
        cardScrollPane.getViewport().setOpaque(false);
        cardScrollPane.getVerticalScrollBar().setUnitIncrement(10);

        decksPanel.setOpaque(false);
        decksPanel.addPropertyChangeListener(
                CardDecksPanel.CP_DECKS_UPDATED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        final int deckCount = (int) evt.getNewValue();
                        decksButton.setText(UiString.get(_S1, deckCount));
                        decksButton.setForeground(deckCount > 0 ? Color.WHITE : Color.GRAY);
                    }
                }
        );

        decksButton.setToolTipText(
                String.format("<html><b>%s</b><br>%s</html>",
                        UiString.get(_S2),
                        UiString.get(_S3)));

        decksButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isImageVisible = !isImageVisible;
                cardScrollPane.setVisible(isImageVisible);
                decksButton.setIsArrowUp(isImageVisible);
                refreshLayout();
            }
        });

        setMinimumSize(new Dimension(cardViewer.getMinimumSize().width, 0));

        setLayout(layout);
        layout.setLayoutConstraints("flowy, insets 0, gap 0");
        refreshLayout();

    }

    private void refreshLayout() {
        removeAll();
        final int minImageHeight = ImageSizePresets.SIZE_312x445.getSize().height + 1;
        add(cardScrollPane, "hmin " + minImageHeight + ", hidemode 3");
        add(decksButton, "w 100%, h 16:24");
        add(decksPanel, "w 100%, growy, pushy, hmin 0");
        revalidate();
    }

    public void setCard(final MagicCardDefinition aCardDef) {

        if (aCardDef == null) {
            return;
        }

        decksButton.setForeground(Color.GRAY);
        decksButton.setText(UiString.get(_S4));

        cardViewer.setCard(aCardDef);
        decksPanel.setCard(aCardDef);

        refreshLayout();

    }

}
