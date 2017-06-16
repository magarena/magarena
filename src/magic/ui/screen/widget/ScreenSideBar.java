package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import magic.model.MagicCardDefinition;
import magic.ui.mwidgets.MScrollPane;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.duel.viewer.DeckEditorCardViewer;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class ScreenSideBar extends TexturedPanel {

    protected final MigLayout migLayout = new MigLayout();
    protected final MScrollPane cardScrollPane = new MScrollPane();
    protected final DeckEditorCardViewer cardViewer = new DeckEditorCardViewer();

    public ScreenSideBar() {
        setLookAndFeel();
    }

    protected void setLookAndFeel() {
        cardScrollPane.setViewportView(cardViewer);
        cardScrollPane.setBorder(null);
        cardScrollPane.setOpaque(false);
        cardScrollPane.setVScrollBarIncrement(10);
        final int BORDER_WIDTH = 1;
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, BORDER_WIDTH, Color.BLACK));
        setMinimumSize(new Dimension(cardViewer.getMinimumSize().width + BORDER_WIDTH, 0));
        setLayout(migLayout);
    }

    public final void setCard(final MagicCardDefinition card) {
        cardViewer.setCard(card);
    }

}
