package magic.ui.screen.card.explorer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import magic.model.MagicCardDefinition;
import magic.translate.MText;
import magic.ui.dialog.prefs.ImageSizePresets;
import magic.ui.mwidgets.MScrollPane;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.card.decks.CardDecksPanel;
import magic.ui.widget.duel.viewer.CardViewer;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ExplorerSideBar extends TexturedPanel {

    // translatable strings
    private static final String _S1 = "decks: %d";
    private static final String _S2 = "List of decks containing this card";
    private static final String _S3 = "Double-click deck name to view complete deck.";
    private static final String _S4 = "searching...";

    private final MigLayout layout = new MigLayout();
    private final CardViewer cardViewer;
    private final MScrollPane cardScrollPane;
    private final CardDecksPanel decksPanel = new CardDecksPanel();
    private final SplitterButton decksButton = new SplitterButton(MText.get(_S1, 0));
    private boolean isImageVisible = true;

    public ExplorerSideBar() {

        // card image viewer
        cardViewer = new CardViewer();

        // card image scroll pane
        cardScrollPane = new MScrollPane();
        cardScrollPane.setViewportView(cardViewer);
        cardScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        cardScrollPane.setOpaque(false);
        cardScrollPane.setVScrollBarIncrement(10);

        decksPanel.setOpaque(false);
        decksPanel.addPropertyChangeListener(CardDecksPanel.CP_DECKS_UPDATED,
                evt -> {
                    final int deckCount = (int) evt.getNewValue();
                    decksButton.setText(MText.get(_S1, deckCount));
                    decksButton.setForeground(deckCount > 0 ? Color.WHITE : Color.GRAY);
                }
        );

        decksButton.setToolTipText(String.format("<html><b>%s</b><br>%s</html>",
                        MText.get(_S2),
                        MText.get(_S3)));

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
        add(cardScrollPane.component(), "hmin " + minImageHeight + ", hidemode 3");
        add(decksButton, "w 100%, h 16:24");
        add(decksPanel, "w 100%, growy, pushy, hmin 0");
        revalidate();
    }

    public void setCard(final MagicCardDefinition aCardDef) {

        if (aCardDef == null) {
            return;
        }

        decksButton.setForeground(Color.GRAY);
        decksButton.setText(MText.get(_S4));

        cardViewer.setCard(aCardDef);
        decksPanel.setCard(aCardDef);

        refreshLayout();

    }

}
