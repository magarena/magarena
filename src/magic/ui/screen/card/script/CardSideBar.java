package magic.ui.screen.card.script;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import magic.model.MagicCardDefinition;
import magic.ui.mwidgets.MScrollPane;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.duel.viewer.DeckEditorCardViewer;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CardSideBar extends TexturedPanel {

    private final MigLayout migLayout = new MigLayout();
    private final MScrollPane cardScrollPane = new MScrollPane();
    private final DeckEditorCardViewer cardViewer = new DeckEditorCardViewer();

    CardSideBar() {
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {

        cardScrollPane.setViewportView(cardViewer);
        cardScrollPane.setBorder(null);
        cardScrollPane.setOpaque(false);
        cardScrollPane.setVScrollBarIncrement(10);

        final int BORDER_WIDTH = 1;
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, BORDER_WIDTH, Color.BLACK));
        setMinimumSize(new Dimension(cardViewer.getMinimumSize().width + BORDER_WIDTH, 0));
        setLayout(migLayout);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        migLayout.setColumnConstraints("[fill, grow]");
        add(cardScrollPane.component());
        revalidate();
    }

    final void setCard(final MagicCardDefinition card) {
        cardViewer.setCard(card);
    }

}
