package magic.ui.explorer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.ui.CardImagesProvider;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.utility.GraphicsUtils;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardPanel extends JPanel {

    private final MigLayout layout = new MigLayout();
    private final CardViewer cardViewer = new CardViewer();
    private final JScrollPane cardScrollPane = new JScrollPane();
    private final CardDecksPanel decksPanel = new CardDecksPanel();
    private final SplitterButton decksButton = new SplitterButton("decks: 0");
    private boolean isImageVisible = true;

    public CardPanel() {

        setOpaque(false);

        // card image viewer
        cardViewer.setPreferredSize(GraphicsUtils.getMaxCardImageSize());
        cardViewer.setMaximumSize(GraphicsUtils.getMaxCardImageSize());

        // card image scroll pane
        cardScrollPane.setViewportView(cardViewer);
        cardScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        cardScrollPane.setOpaque(false);
        cardScrollPane.getViewport().setOpaque(false);
        cardScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        cardScrollPane.setHorizontalScrollBarPolicy(
                GeneralConfig.getInstance().isHighQuality()
                        ? ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
                        : ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        decksPanel.setOpaque(false);
        decksPanel.addPropertyChangeListener(
                CardDecksPanel.CP_DECKS_UPDATED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        final int deckCount = (int) evt.getNewValue();
                        decksButton.setText("decks: " + deckCount);
                        decksButton.setForeground(deckCount > 0 ? Color.WHITE : Color.GRAY);
                    }
                }
        );

        decksButton.setToolTipText("<html><b>List of decks containing this card</b><br>Double-click deck name to view complete deck.</html>");
        decksButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isImageVisible = !isImageVisible;
                cardScrollPane.setVisible(isImageVisible);
                decksButton.setIsArrowUp(isImageVisible);
                refreshLayout();
            }
        });

        setLayout(layout);
        layout.setLayoutConstraints("flowy, insets 0, gap 0");
        refreshLayout();

    }

    private void refreshLayout() {
        removeAll();
        final int minImageHeight = CardImagesProvider.SMALL_SCREEN_IMAGE_SIZE.height + 1;
        add(cardScrollPane, "hmin " + minImageHeight + ", hidemode 3");
        add(decksButton, "w 100%, h 16:24");
        add(decksPanel, "w 100%, growy, pushy, hmin 0");
        revalidate();
    }

    public void setCard(final MagicCardDefinition aCardDef) {

        if (aCardDef == null) {
            return;
        }

        cardViewer.setCard(aCardDef);
        decksPanel.setCard(aCardDef);

        refreshLayout();
        
    }

}
